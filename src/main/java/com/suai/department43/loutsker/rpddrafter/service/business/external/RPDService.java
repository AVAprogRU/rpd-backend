package com.suai.department43.loutsker.rpddrafter.service.business.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.enumeration.FileExtension;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.DisciplineEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.RPDEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Teacher;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.UniversityPosition;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Competence;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Discipline;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.RPD;
import com.suai.department43.loutsker.rpddrafter.domain.payload.*;
import com.suai.department43.loutsker.rpddrafter.exception.business.*;
import com.suai.department43.loutsker.rpddrafter.service.business.internal.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
public class RPDService implements Initializer, DepartmentManager, Drafter, Technician {
    private final DBProvider provider;
    private final FileHelper fileHelper;
    private final DocumentDrafter drafter;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final JsonConverter jsonConverter;
    private final RegistrationService registrationService;

    private XWPFDocument templateDocument;
    private XSSFWorkbook teacherInfoDocument;
    private UMODTO disciplinesJson;

    private final String templatePath;
    private final String teacherInfoPath;
    private final String temporalPath;
    private final String translationMapPath;
    private final String samplesMapPath;
    private final String umoFilePath;

    @Autowired
    public RPDService(@Value("${file-system.rpd-template.path}") String templatePath,
                      @Value("${file-system.teacher-info-file.path}") String teacherInfoPath,
                      @Value("${file-system.temporal-file.path}") String temporalPath,
                      @Value("${file-system.translation-map.path}") String translationMapPath,
                      @Value("${file-system.samples-map.path}") String samplesMapPath,
                      @Value("${file-system.umo-file.path}") String umoFilePath,
                      DBProvider provider, FileHelper fileHelper, EmailService emailService,
                      RegistrationService registrationService, JsonConverter jsonConverter,
                      ObjectMapper objectMapper, DocumentDrafter drafter) {
        this.drafter = drafter;
        this.provider = provider;
        this.fileHelper = fileHelper;
        fileHelper.setTempPath(temporalPath);
        this.emailService = emailService;
        this.objectMapper = objectMapper;
        this.jsonConverter = jsonConverter;
        this.registrationService = registrationService;
        this.templatePath = templatePath;
        this.temporalPath = temporalPath;
        this.teacherInfoPath = teacherInfoPath;
        this.translationMapPath = translationMapPath;
        this.samplesMapPath = samplesMapPath;
        this.umoFilePath = umoFilePath;
        loadPresetTemplate();
        loadPresetTranslationMap();
        loadPresetSamples();
        loadPresetTeachers();
        loadPresetDisciplines();
    }

    // RPD handling methods \\
    @Override
    public RPDEntity getImportData(long rpdId) {
        try {
            RPDEntity entity = provider.getRPDById(rpdId);
            RPD rpd = entity.getBody();
            // get list of teacher input based placeholders in current template
            List<String> templatePlaceholderNames = getTemplateInputPlaceholderNames();
            // get list of placeholders from the rpd
            Set<String> rpdPlaceholderNames = rpd.getPlaceholders().keySet();
            // combine, leave those placeholders that are present in the template based on name
            HashMap<String, String> resultPlaceholders = new HashMap<>();
            for (String placeholder : rpdPlaceholderNames) {
                if (templatePlaceholderNames.contains(placeholder)) {
                    resultPlaceholders.put(placeholder, rpd.getPlaceholders().get(placeholder));
                }
            }
            // get list of table dtos from the rpd
            List<TableDataDTO> rpdTables = rpd.getTables();
            // get list of table dtos from the template
            XWPFDocument templateCopy = fileHelper.copyXWPFDocument(templateDocument);
            RPD forCompetenceTable = new RPD();
            forCompetenceTable.setCompetences(rpd.getCompetences());
            int fontSize = drafter.getFontSize();
            int competenceTableIndex = drafter.getFirstDisciplineContentTableIndex() - 2;
            drafter.fillOutCompetencesTable(templateCopy, competenceTableIndex, fontSize, forCompetenceTable);
            List<TableDataDTO> templateTables = drafter.getDocumentTablesData(templateCopy);
            // combine, leave those tables that are present in the template based on number in name + headers number
            List<TableDataDTO> resultTables = new ArrayList<>();
            for (TableDataDTO templateTable : templateTables) {
                boolean matched = false;
                for (Iterator<TableDataDTO> iterator = rpdTables.iterator(); iterator.hasNext();) {
                    TableDataDTO rpdTable = iterator.next();
                    if (headersEqual(templateTable.getHeaders(), rpdTable.getHeaders())) {
                        templateTable.setBody(rpdTable.getBody());
                        resultTables.add(rpdTable);
                        iterator.remove();
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    resultTables.add(templateTable);
                }
            }
            rpd.setPlaceholders(resultPlaceholders);
            rpd.setTables(resultTables);
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Enable to import");
        }
    }

    @Override
    public FileDTO getReDraftedDocument(long teacherId, long rpdId, TeacherInputDTO input) {
        if (templateDocument == null) {
            throw new TemplateNotFoundException("Please, setup template");
        }
        loadPresetTemplate();
        try {
            RPDEntity rpdEntity = provider.getRPDById(rpdId);
            RPD rpd = rpdEntity.getBody();
            rpd.getPlaceholders().putAll(input.getPlaceholders());
            rpd.setTables(input.getTables());

            drafter.setupRPDToDraft(rpd);
            drafter.setupPlaceholdersMap(rpd.getPlaceholders());
            XWPFDocument templateCopy = new XWPFDocument(templateDocument.getPackage());
            XWPFDocument draftedDocument = drafter.draftDocument(templateCopy);
            String binaryString = fileHelper.convertToBase64(draftedDocument);

            return new FileDTO(binaryString);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DraftFailureException(ex.getMessage());
        }
    }

    @Override
    public FileDTO getDraftedDocument(long teacherId, long disciplineId, TeacherInputDTO input) {
        if (templateDocument == null) {
            throw new TemplateNotFoundException("Please, setup template");
        }
        loadPresetTemplate();
        // loading objects
        Teacher teacher = provider.getTeacherById(teacherId);
        DisciplineEntity disciplineEntity = provider.getDisciplineById(disciplineId);
        Discipline discipline = disciplineEntity.getBody();
        // rpd runtime
        RPD rpd = new RPD();
        addSharedFieldsToRPD(rpd, discipline); // point to separate constant placeholders from teacher ones
        addConstantsToRPD(rpd, teacher, discipline);
        addCompetencesStringified(rpd);
        addTeacherInput(rpd, input);
        // rpd persistence
        RPDEntity rpdVersion = new RPDEntity();
        rpdVersion.setBody(rpd);
        addTitlePageProperties(rpdVersion, teacher, discipline);
        // rpd document draft
        try {
            drafter.setupRPDToDraft(rpd);
            drafter.setupPlaceholdersMap(rpd.getPlaceholders());
            XWPFDocument templateCopy = new XWPFDocument(templateDocument.getPackage());
            XWPFDocument draftedDocument = drafter.draftDocument(templateCopy);
            String binaryString = fileHelper.convertToBase64(draftedDocument);
            drafter.setupTables(input, rpd, templateCopy);
            provider.saveRPD(rpdVersion);

            return new FileDTO(binaryString);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DraftFailureException(ex.getMessage());
        }
    }

    @Override
    public List<RPDEntity> getRPDVersionsByProperties(String disciplineName, String programCode, int enrollYear, String authorName) {
        return provider.getRPDByProperties(disciplineName, programCode, enrollYear, authorName);
    }

    @Override
    public List<RPDEntity> getAllRPDs() {
        return provider.getAllRPDs();
    }

    @Override
    public RPDEntity getRPDById(long id) {return provider.getRPDById(id);}

    @Override
    public void deleteRPDById(long id) {provider.deleteRPDById(id);}

    // Department management methods \\
    @Override
    public List<DisciplineEntity> getDisciplines() {
        return provider.getDisciplines();
    }

    @Override
    public List<Teacher> getTeachers() {
        return provider.getTeachers();
    }

    @Override
    public void assignDisciplineOnTeacher(long teacherId, long disciplineId, String note) {
        Teacher teacher = provider.getTeacherById(teacherId);
        DisciplineEntity disciplineEntity = provider.getDisciplineById(disciplineId);
        List<String> assignedDisciplineNames = teacher.getAssignedDisciplines().stream()
                .map(DisciplineEntity::getName)
                .toList();
        if (assignedDisciplineNames.contains(disciplineEntity.getName())) {
            throw new AlreadyAssignedException(teacher.getName(), disciplineEntity.getName());
        }
        teacher.getAssignedDisciplines().add(disciplineEntity);
        provider.saveTeacher(teacher);
        String extendedNote;
        if (!provider.teacherHasAccount(teacher)) {
            String credentialString = registrationService.registerTeacher(teacher);
            extendedNote = getExtendedRPDAssignmentNote(note, disciplineEntity.getName(), credentialString);
        } else {
            extendedNote = getNoCredentialsExtendedNote(note, disciplineEntity.getName());
        }
        emailService.sendEmail(teacher.getEmail(), getAssignmentNotificationSubject(), extendedNote);
    }

    // Application setup methods \\
    @Override
    public void setupTemplate(FileDTO dto) {
        File file = fileHelper.extractDocumentFromBase64Format(dto.getBinaryString(), FileExtension.DOCX);
        try {
            InputStream inStream = new FileInputStream(file);
            templateDocument = new XWPFDocument(inStream);
            inStream.close();
            FileOutputStream outStream = new FileOutputStream(templatePath);
            templateDocument.write(outStream);
            outStream.close();
        } catch (Exception ex) {
            throw new FileHandlingException("Enable to setup .docx file on specified path + '" + templatePath + "'");
        }
    }

    @Override
    public void setupTeachersFile(FileDTO dto) {
        File file = fileHelper.extractDocumentFromBase64Format(dto.getBinaryString(), FileExtension.XLSX);
        try {
            teacherInfoDocument = new XSSFWorkbook(file);
            FileOutputStream outStream = new FileOutputStream(teacherInfoPath);
            teacherInfoDocument.write(outStream);
            outStream.close();
        } catch (Exception ex) {
            throw new FileHandlingException("Enable to setup .xlsx file on specified path + '" + teacherInfoPath + "'");
        }
        saveTeachersFromDocument(teacherInfoDocument);
    }

    @Override
    public void setupDisciplinesData(UMODTO umodto) {
        List<Discipline> disciplineDTOS = jsonConverter.extractDisciplineDTOsFromUMODTO(umodto);
        List<DisciplineEntity> disciplineEntities = jsonConverter.extractDisciplineFromDisciplineDto(disciplineDTOS);
        provider.saveDisciplineList(disciplineEntities);
        try {
            objectMapper.writeValue(new File(umoFilePath), umodto);
        } catch (Exception ex) {
            throw new FileHandlingException("Enable to setup .json file on specified path + '" + umoFilePath + "'");
        }
    }

    @Override
    public void setupTranslationMap(HashMap<String, String> translationMap) {
        try {
            objectMapper.writeValue(new File(translationMapPath), translationMap);
        } catch (Exception ex) {
            throw new FileHandlingException("Enable to setup .json file on specified path + '" + translationMapPath + "'");
        }
        drafter.setupTranslationMap(translationMap);
    }

    @Override
    public void setupGenerals(int fontSize, int firstDisciplineContentTableIndex, int firstAssessmentTableIndex) {
        drafter.setupFontSize(fontSize);
        drafter.setupTableIndexes(firstDisciplineContentTableIndex, firstAssessmentTableIndex);
    }

    @Override
    public void setupSamplesMap(HashMap<String, String> samplesMap) {
        try {
            objectMapper.writeValue(new File(samplesMapPath), samplesMap);
        } catch (Exception ex) {
            throw new FileHandlingException("Enable to setup .json file on specified path + '" + samplesMapPath + "'");
        }
    }

    @Override
    public HashMap<String, String> getTranslationMap() {
        return drafter.getTranslationMap();
    }

    @Override
    public HashMap<String, String> getSamplesMap() {
        try {
            return objectMapper.readValue(new File(samplesMapPath), HashMap.class);
        } catch (Exception ex) {
            throw new FileHandlingException("Samples file is not provided. Enable to read .json file on specified path + '" + samplesMapPath + "'");
        }
    }

    @Override
    public FileDTO getTemplateFile() {
        try {
            return new FileDTO(fileHelper.convertToBase64(templateDocument));
        } catch (Exception ex) {
            throw new FileHandlingException("Template file is not provided");
        }
    }

    @Override
    public FileDTO getTeachersFile() {
        try {
            return new FileDTO(fileHelper.convertToBase64(teacherInfoDocument));
        } catch (Exception ex) {
            throw new FileHandlingException("Teachers file is not provided");
        }
    }

    @Override
    public FileDTO getDisciplinesFile() {
        try {
            String json = objectMapper.writeValueAsString(disciplinesJson);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return new FileDTO(base64);
        } catch (Exception ex) {
            throw new FileHandlingException("UMO file is not provided");
        }
    }

    @Override
    public FileDTO getTranslationsFile() {
        try {
            String json = objectMapper.writeValueAsString(drafter.getTranslationMap());
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return new FileDTO(base64);
        } catch (Exception ex) {
            throw new FileHandlingException("Translations file is not provided");
        }
    }

    @Override
    public FileDTO getSamplesFile() {
        HashMap<String, String> map = getSamples();
        try {
            return new FileDTO(fileHelper.convertToBase64(map));
        } catch (Exception ex) {
            throw new FileHandlingException("Samples file is not provided");
        }
    }

    // Technical methods \\
    @Override
    public List<TableDataDTO> getRPDTablesForDiscipline(long disciplineId) {
        if (templateDocument == null) {
            throw new FileHandlingException("Template is not provided");
        }

        try {
            XWPFDocument templateCopy = fileHelper.copyXWPFDocument(templateDocument);
            Discipline discipline = provider.getDisciplineById(disciplineId).getBody();
            RPD rpd = new RPD();
            jsonConverter.merge(rpd, discipline);

            // Инициализируем список для хранения состояния таблиц
            List<TableDataDTO> currentTables = new ArrayList<>();

            // Логируем начальное состояние (должно быть пустым)
            currentTables = drafter.getDocumentTablesData(templateCopy);
            logTablesState("Initial state (before any filling)", currentTables);

            // 1. Заполняем таблицу интенсивности
            drafter.fillOutIntensityTable(templateCopy, drafter.getFirstDisciplineContentTableIndex() - 1, 3, rpd, drafter.getTranslationMap());
            currentTables = drafter.getDocumentTablesData(templateCopy);
            logTablesState("After fillOutIntensityTable", currentTables);

            // 2. Заполняем таблицу компетенций
            drafter.fillOutCompetencesTable(templateCopy, drafter.getFirstDisciplineContentTableIndex() - 2, drafter.getFontSize(), rpd);
            currentTables = drafter.getDocumentTablesData(templateCopy);
            logTablesState("After fillOutCompetencesTable", currentTables);

            // 3. Заполняем таблицу аттестации
            drafter.fillOutAttestationTable(templateCopy, drafter.getFirstAssessmentTableIndex(), rpd);
            currentTables = drafter.getDocumentTablesData(templateCopy);
            logTablesState("After fillOutAttestationTable", currentTables);

            // 4. Расширяем таблицу, зависящую от семестра
            drafter.expandTermDependentTable(templateCopy, drafter.getFirstDisciplineContentTableIndex() + 4, rpd);
            currentTables = drafter.getDocumentTablesData(templateCopy);
            logTablesState("After expandTermDependentTable", currentTables);

            return currentTables;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to generate RPD tables", ex);
        }
    }

    // Метод для логирования состояния таблиц
    private void logTablesState(String stageName, List<TableDataDTO> tables) {
        try {
            System.out.println("\n=== " + stageName + " ===");
            System.out.println("Number of tables: " + tables.size());

            for (int i = 0; i < tables.size(); i++) {
                System.out.println("\nTable #1):");
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tables.get(i)));
            }
        } catch (JsonProcessingException e) {
            System.err.println("JSON serialization error: " + e.getMessage());
        }
    }


    @Override
    public DisciplineEssentialsDTO getDisciplineEssentials(long disciplineId) {
        DisciplineEntity disciplineEntity = provider.getDisciplineById(disciplineId);
        Discipline discipline = disciplineEntity.getBody();
        return jsonConverter.extractEssentials(discipline, drafter.getTranslationMap());
    }

    @Override
    public List<String> getTemplateInputPlaceholderNames() {
        if (templateDocument == null) {
            throw new FileHandlingException("Template is not provided");
        }

        Discipline discipline = provider.getDisciplines().get(0).getBody();
        Set<String> autoFilledPlaceholders = jsonConverter.collectFields(discipline).keySet();
        List<String> allTemplatePlaceholders = drafter.getTemplatePlaceholders(templateDocument);

        List<String> inputBasedPlaceholders = new ArrayList<>();
        for (String placeholder : allTemplatePlaceholders) {
            if (!autoFilledPlaceholders.contains(placeholder)) {
                inputBasedPlaceholders.add(placeholder);
            }
        }

        // constant placeholders \\
        inputBasedPlaceholders.removeIf(element -> element.equals("current_year"));
        inputBasedPlaceholders.removeIf(element -> element.equals("year"));
        inputBasedPlaceholders.removeIf(element -> element.equals("city"));
        inputBasedPlaceholders.removeIf(element -> element.equals("competences"));
        inputBasedPlaceholders.removeIf(element -> element.equals("education_format"));
        inputBasedPlaceholders.removeIf(element -> element.equals("teacher"));
        inputBasedPlaceholders.removeIf(element -> element.equals("current_year"));
        inputBasedPlaceholders.removeIf(element -> element.equals("teacher_position"));

        return inputBasedPlaceholders;
    }

    @Override
    public HashMap<String, String> getSamples() {
        try {
            return objectMapper.readValue(new File(samplesMapPath), HashMap.class);
        } catch (Exception ex) {
            return new HashMap<String, String>();
        }
    }

    @Override
    public int getFontSize() {
        return drafter.getFontSize();
    }

    @Override
    public int getFirstDisciplineContentTableIndex() {
        return drafter.getFirstDisciplineContentTableIndex();
    }

    @Override
    public int getFirstAssessmentTableIndex() {
        return drafter.getFirstAssessmentTableIndex();
    }

    // Private methods \\
    private boolean headersEqual(List<String> headers1, List<String> headers2) {
        if (headers1.size() != headers2.size()) {
            return false;
        }
        for (int i = 0; i < headers1.size(); i++) {
            if (!headers1.get(i).equals(headers2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void addConstantsToRPD(RPD rpd, Teacher teacher, Discipline discipline) {
        HashMap<String, String> placeholders = rpd.getPlaceholders();
        placeholders.put("year", String.valueOf(discipline.getEnrollYear()));
        placeholders.put("current_year", String.valueOf(LocalDate.now().getYear()));
        placeholders.put("city", "Санкт-Петербург");
        placeholders.put("teacher_position", jsonConverter.getPositionFormattedName(teacher.getPosition()));
        placeholders.put("teacher", jsonConverter.getTeacherFormattedName(teacher));
        String format = "";
        if (rpd.isEveningEducationFormat()) {
            format = "очно-заочная";
        }
        if (rpd.isFullEducationFormat()) {
            format = "очная";
        }
        if (rpd.isExtramuralEducationFormat()) {
            format = "заочная";
        }
        placeholders.put("education_format", format);
    }

    private void addCompetencesStringified(RPD rpd) {
        String competenceString = "";
        List<Competence> competences = rpd.getCompetences();
        for (Competence competence : competences) {
            competenceString += competence.getName() + " " + competence.getDescription() + "\n";
        }
        rpd.getPlaceholders().put("competences", competenceString);
    }

    private void addSharedFieldsToRPD(RPD rpd, Discipline discipline) {
        jsonConverter.merge(rpd, discipline);
    }

    private void addTeacherInput(RPD rpd, TeacherInputDTO inputDTO) {
        rpd.getPlaceholders().putAll(inputDTO.getPlaceholders());
        rpd.getTables().addAll(inputDTO.getTables());
    }

    private void addTitlePageProperties(RPDEntity rpdVersion, Teacher teacher, Discipline discipline) {
        rpdVersion.getBody().getPlaceholders().put("teacher_position", jsonConverter.getPositionFormattedName(teacher.getPosition()));
        rpdVersion.getBody().getPlaceholders().put("teacher", jsonConverter.getTeacherFormattedName(teacher));
        rpdVersion.getBody().getPlaceholders().put("discipline_name", discipline.getDisciplineName());
        rpdVersion.getBody().getPlaceholders().put("year", String.valueOf(discipline.getEnrollYear()));

        rpdVersion.setAuthorPosition(jsonConverter.getPositionFormattedName(teacher.getPosition()));
        rpdVersion.setAuthorName(jsonConverter.getTeacherFormattedName(teacher));
        rpdVersion.setDisciplineName(discipline.getDisciplineName());
        rpdVersion.setEnrollYear(discipline.getEnrollYear());
        rpdVersion.setProgramCode(discipline.getProgramCode());
    }

    private void loadPresetDisciplines() {
        try {
            File file = new File(umoFilePath);
            disciplinesJson = objectMapper.readValue(file, UMODTO.class);
        } catch (Exception ex) {
            System.out.println("Disciplines file not found on startup");
        }
    }

    private void loadPresetTeachers() {
        try {
            File file = new File(templatePath);
            InputStream inStream = new FileInputStream(file);
            teacherInfoDocument = new XSSFWorkbook(inStream);
            inStream.close();
        } catch (Exception ex) {
            System.out.println("Teachers file not found on startup");
        }
    }

    private void loadPresetTemplate() {
        try {
            File file = new File(templatePath);
            InputStream inStream = new FileInputStream(file);
            templateDocument = new XWPFDocument(inStream);
            inStream.close();
        } catch (Exception ex) {
            System.out.println("Template file not found on startup");
        }
    }

    private void loadPresetTranslationMap() {
        try {
            File file = new File(translationMapPath);
            HashMap<String, String> map = objectMapper.readValue(file, HashMap.class);
            drafter.setupTranslationMap(map);
        } catch (Exception ex) {
            System.out.println("Translations file not found on startup");
        }
    }

    private void loadPresetSamples() {
        try {
            File file = new File(samplesMapPath);
            HashMap<String, String> map = objectMapper.readValue(file, HashMap.class);
            setupSamplesMap(map);
        } catch (Exception ex) {
            System.out.println("Samples file not found on startup");
        }
    }

    private void saveTeachersFromDocument(XSSFWorkbook teachersInfoDocument) {
        List<Teacher> newTeachers = extractTeachersFromDocument(teachersInfoDocument);
        List<Teacher> currentTeacherList = provider.getTeachers();
        for (Teacher newbie : newTeachers) {
            if (!currentTeacherList.contains(newbie)) {
                provider.savePosition(newbie.getPosition());
                provider.saveTeacher(newbie);
            }
        }

        currentTeacherList = provider.getTeachers();
        for (Teacher teacher : currentTeacherList) {
            if (!newTeachers.contains(teacher)) {
                provider.deleteTeacherById(teacher.getId());
            }
        }
    }

    private List<Teacher> extractTeachersFromDocument(XSSFWorkbook teachersInfoDocument) {
        try {
            List<Teacher> teachers = new ArrayList<>();
            XSSFSheet sheet = teachersInfoDocument.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                Teacher teacher = new Teacher();
                UniversityPosition position = new UniversityPosition();
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String cellString;
                    try {
                        cellString = cell.getStringCellValue();
                    } catch (NullPointerException ex) {
                        cellString = "";
                    }
                    switch (j) {
                        case 0 -> teacher.setLastname(cellString);
                        case 1 -> teacher.setName(cellString);
                        case 2 -> teacher.setPatronymic(cellString);
                        case 3 -> position.setPosition(cellString);
                        case 4 -> position.setAcademicDegree(cellString);
                        case 5 -> position.setScientificTitle(cellString);
                        case 6 -> teacher.setEmail(cellString);
                    }
                }
                teacher.setPosition(position);
                teachers.add(teacher);
            }
            return teachers;
        } catch (Exception ex) {
            throw new FileDataExtractionException("XLSX");
        }
    }

    private String getExtendedRPDAssignmentNote(String note, String disciplineName, String credentials) {
        return "Greetings!\n" +
                "You have been assigned to draft RPD for \"" + disciplineName + "\" discipline.\n" +
                "Note on the assignment:\n" +
                note + "\n" + "Your credentials: " + credentials;
    }

    private String getNoCredentialsExtendedNote(String note, String disciplineName) {
        return "Greetings!\n" +
                "You have been assigned to draft RPD for \"" + disciplineName + "\" discipline.\n" +
                "Note on the assignment:\n" +
                note;
    }

    private String getAssignmentNotificationSubject() {
        return "SUAI RPD assignment";
    }
}
