package com.suai.department43.loutsker.rpddrafter.service.business.internal;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.enumeration.FirstLineIndent;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.AchievementIndicator;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Competence;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.RPD;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Term;
import com.suai.department43.loutsker.rpddrafter.domain.payload.*;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;

@Component
public class DocumentDrafter {
    private final String defaultPlaceholder = "default";
    private RPD rpdToDraft;
    private HashMap<String, String> translationMap;
    private HashMap<String, String> placeholdersMap;

    private int fontSize = 12;
    private int firstDisciplineContentTableIndex = 7;
    private int firstAssessmentTableIndex = 17;

    public DocumentDrafter() {
        translationMap = new HashMap<>();
        placeholdersMap = new HashMap<>();
    }

    public XWPFDocument draftDocument(XWPFDocument documentToDraft) {
        replaceTextInTables(documentToDraft);
        replaceTextInParagraphs(documentToDraft);
        fillOutIntensityTable(documentToDraft, firstDisciplineContentTableIndex - 1, 3, rpdToDraft, translationMap);
        fillOutCompetencesTable(documentToDraft, firstDisciplineContentTableIndex - 2, fontSize, rpdToDraft);
        fillOutAttestationTable(documentToDraft, firstAssessmentTableIndex, rpdToDraft);
        expandTermDependentTable(documentToDraft, firstDisciplineContentTableIndex + 4, rpdToDraft);
        fillOutGenericTables(documentToDraft, firstDisciplineContentTableIndex);
        //
        /*try {
            FileOutputStream outStream = new FileOutputStream("C:/drafter/workspace/rpd-template.docx");
            documentToDraft.write(outStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        //
        return documentToDraft;
    }

    /**
     * Эта функция берет все таблицы до firstDisciplineContentTableIndex из первого списка, а остальные из второго.
     * Используется во время импорта, чтоб основные данные брались из УМО, а остальное из выбранной РПД
    * @param
    *
    * */
    public List<TableDataDTO> mergeTables(List<TableDataDTO> newTables, List<TableDataDTO> existingTables) {

        List<TableDataDTO> merged = new ArrayList<>();

        merged.addAll(newTables.subList(0, firstDisciplineContentTableIndex));
        merged.addAll(existingTables.subList(firstDisciplineContentTableIndex, existingTables.size()));

        return merged;
    }

    public void setupTables(TeacherInputDTO input, RPD rpd, XWPFDocument document) {
        List<TableDataDTO> allDocumentTables = getDocumentTablesData(document);
        List<TableDataDTO> inputBasedTables = input.getTables();

        List<TableDataDTO> tables = new ArrayList<>();
        int inputTablesOffset = firstDisciplineContentTableIndex - 2;

        tables.addAll(allDocumentTables.subList(0, 3));
        tables.addAll(inputBasedTables.subList(1, firstAssessmentTableIndex - inputTablesOffset - 2)); // -2
        tables.addAll(allDocumentTables.subList(firstAssessmentTableIndex - inputTablesOffset, firstAssessmentTableIndex - inputTablesOffset + 2));
        tables.addAll(inputBasedTables.subList(firstAssessmentTableIndex - inputTablesOffset, inputBasedTables.size()));

        rpd.setTables(tables);
    }

    public List<TableDataDTO> getDocumentTablesData(XWPFDocument document) {
        List<XWPFTable> documentTables = document.getTables();
        List<XWPFTable> contentTables = documentTables.subList(firstDisciplineContentTableIndex - 2, documentTables.size() - 1);

        List<String> paragraphs = getParagraphsAboveTables(document, contentTables);

        List<TableDataDTO> data = new ArrayList<>();
        for (String p : paragraphs) {
            TableDataDTO dto = new TableDataDTO();
            dto.setName(p);
            data.add(dto);
        }

        for (int j = 0; j < contentTables.size(); j++) {
            TableDataDTO dto = data.get(j);
            XWPFTable table = contentTables.get(j);
            dto.setHeaders(getTableHeaders(table));
            dto.setBody(getDTO(table));
        }

        return data;
    }

    public TableDTO getDTO(XWPFTable table) {
        List<List<String>> body = new ArrayList<>();
        if (table.getRows().size() > 1) {
            for (XWPFTableRow row : table.getRows().subList(1, table.getRows().size())) {
                List<String> tableRow = new ArrayList<>();
                for (XWPFTableCell cell : row.getTableCells()) {
                    String cellText = "";
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            cellText += run.text();
                        }
                    }
                    tableRow.add(cellText);
                }
                body.add(tableRow);
            }
        }

        return new TableDTO(body);
    }

    public List<String> getTableHeaders(XWPFTable table) {
        List<String> headers = new ArrayList<>();
        // Get the first row of the table
        if (table.getRows().size() > 0) {
            XWPFTableRow headerRow = table.getRow(0);
            // Iterate through the cells of the first row
            for (XWPFTableCell cell : headerRow.getTableCells()) {
                headers.add(cell.getText());
            }
        }
        return headers;
    }

    public List<String> getParagraphsAboveTables(XWPFDocument document, List<XWPFTable> tables) {
        List<String> paragraphsAboveTables = new ArrayList<>();
        // Get all elements in the document
        List<IBodyElement> elements = document.getBodyElements();
        for (XWPFTable table : tables) {
            // Find the table in the list of elements
            for (int i = 0; i < elements.size(); i++) {
                IBodyElement element = elements.get(i);
                if (element instanceof XWPFTable && element.equals(table)) {
                    // Look for the first paragraph before the table
                    for (int j = i - 1; j >= 0; j--) {
                        IBodyElement previousElement = elements.get(j);
                        if (previousElement instanceof XWPFParagraph) {
                            XWPFParagraph paragraph = (XWPFParagraph) previousElement;
                            paragraphsAboveTables.add(paragraph.getText());
                            break; // Found the paragraph above the table
                        }
                    }
                }
            }
        }
        return paragraphsAboveTables;
    }

    public List<String> getTemplatePlaceholders(XWPFDocument template) {
        List<String> placeholders = new ArrayList<>();
        List<XWPFParagraph> paragraphs = new ArrayList<>(template.getParagraphs());

        List<XWPFTable> tables = template.getTables();
        List<XWPFTableRow> rows = new ArrayList<>();
        for (XWPFTable table : tables) {
            rows.addAll(table.getRows());
        }
        List<XWPFTableCell> cells = new ArrayList<>();
        for (XWPFTableRow row : rows) {
            cells.addAll(row.getTableCells());
        }
        List<XWPFParagraph> tableParagraphs = new ArrayList<>();
        for (XWPFTableCell cell : cells) {
            if (cell.getParagraphs() != null) {
                tableParagraphs.addAll(cell.getParagraphs());
            }
        }

        paragraphs.addAll(tableParagraphs);

        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runList = paragraph.getRuns();
            if (runList.isEmpty()) {
                continue;
            }
            int runListSize = runList.size();
            String runText = paragraph.getText();
            int varInRunNum = runText.split("\\$").length - 1;
            for (int i = 0; i < varInRunNum; i++) {
                int firstRunWithVarIndex = -1;
                for (XWPFRun run : runList) {
                    firstRunWithVarIndex++;
                    if (run.text().contains("${")) {
                        String combinedText = "";
                        String text = "";
                        for (int j = firstRunWithVarIndex; j < runListSize; j++) {
                            text = runList.get(j).text();
                            combinedText += text;
                            if (text.contains("}")) {
                                break;
                            }
                        }
                        int startOfVar = combinedText.indexOf("$");
                        int endOfVar = combinedText.indexOf("}");
                        String variable = combinedText.substring(startOfVar + 2, endOfVar);

                        if (requiredBulletListOutput(variable) || requiredMultiLineOutput(variable)) {
                            placeholders.add(variable.substring(1, variable.length() - 1));
                        } else {
                            placeholders.add(variable);
                        }
                    }
                }
            }
        }
        return placeholders;
    }

    public void setupFontSize(int size) {
        fontSize = size;
    }

    public void setupTableIndexes(int firstDisciplineContentTableIndex, int firstAssessmentTableIndex) {
        this.firstDisciplineContentTableIndex = firstDisciplineContentTableIndex;
        this.firstAssessmentTableIndex = firstAssessmentTableIndex;
    }

    public void setupRPDToDraft(RPD rpdToDraft) {
        this.rpdToDraft = rpdToDraft;
    }

    public void setupTranslationMap(HashMap<String, String> translationMap) {
        this.translationMap.clear();
        this.translationMap.putAll(translationMap);
    }

    public void setupPlaceholdersMap(HashMap<String, String> placeholdersMap) {
        this.placeholdersMap.clear();
        this.placeholdersMap.putAll(placeholdersMap);
    }

    public HashMap<String, String> getTranslationMap() {
        return translationMap;
    }

    public HashMap<String, String> getPlaceholdersMap() {
        return placeholdersMap;
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getFirstDisciplineContentTableIndex() {
        return firstDisciplineContentTableIndex;
    }

    public int getFirstAssessmentTableIndex() {
        return firstAssessmentTableIndex;
    }

    /**
     * Fills out intensity table by its number in the given document.
     * @param document the document to work with
     * @param tableNumber table sequence number (7)
     * @param numberOfFirstComingProperties number of properties to put before
     * the different types of classroom activities (3)
     */
    public void fillOutIntensityTable(XWPFDocument document, int tableNumber, int numberOfFirstComingProperties,
                                      RPD rpdToDraft, HashMap<String, String> translationMap) {
        List<XWPFTable> documentTables = document.getTables();
        if (documentTables.isEmpty() || documentTables.size() <= tableNumber) {
            return;
        }
        // Get discipline intensity table
        XWPFTable table = documentTables.get(tableNumber);
        // Expand table
        table.setWidth("100%");
        table.getRow(0).getCell(0).setWidth("40%");
        List<Term> terms = rpdToDraft.getTerms();
        Term totalTerm = rpdToDraft.getTotalTerm();
        List<Object> totalTermData = new ArrayList<>(totalTerm.getPrimitiveFieldData());
        // Remove term's number
        totalTermData.remove(0);
        // List of classroom activities present in the terms
        HashMap<String, Integer> presentActivities = new HashMap<>();
        for (String key : totalTerm.getClassroomActivities().keySet()) {
            if (totalTerm.getClassroomActivities().get(key) != 0) {
                presentActivities.put(key, totalTerm.getClassroomActivities().get(key));
            }
        }
        int numberOfClassroomActivities = presentActivities.size();
        int numberOfTermProperties = totalTermData.size() + numberOfClassroomActivities;
        // Add rows
        for (int j = 0; j < numberOfTermProperties + 1; j++) {
            table.createRow();
        }
        // Add columns
        int numberOfTerms = terms.size();
        //
        for (int j = 0; j < numberOfTerms - 1; j++) {
            table.addNewCol();
            // The method behavior is unknown (but works properly)
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(2000));
        }
        int numberOfColumns = table.getRow(0).getTableCells().size();
        // Merge 'term intensity' section
        try {
            mergeCellHorizontally(table, 0, 2, numberOfColumns - 1);
        } catch (Exception ex) {
            System.out.println("Failed to merge cells: " + ex.getMessage());
        }
        // Column and term numbering
        for (int j = 0; j < numberOfColumns; j++) {
            fillTableCell(table, 2, j, String.valueOf(j + 1), fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
        }
        List<String> termNumbers = new ArrayList<>();
        //
        for (int j = 0; j < numberOfTerms; j++) {
            termNumbers.add("№" + terms.get(j).getNumber());
        }
        for (int j = 2; j < numberOfColumns; j++) {
            fillTableCell(table, 1, j, termNumbers.get(j - 2), fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
        }
        terms.add(0,totalTerm);
        List<Field> listOfFields = new ArrayList<>(Arrays.stream(Term.class.getDeclaredFields()).toList());
        // remove id and number fields
        listOfFields.remove(0);
        listOfFields.remove(0);
        // first coming properties
        int rowIndex = 3;
        int primitivePropIndex = 1;
        for (int j = 0; j < numberOfFirstComingProperties; rowIndex++, j++, primitivePropIndex++) {
            String text = translationMap.get(listOfFields.get(j).getName());
            fillTableCell(table, rowIndex, 0, text, fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.ZERO);
            for (int column = 1; column < numberOfColumns; column++) {
                Term term = terms.get(column - 1);
                String hours = String.valueOf(term.getPrimitiveFieldData().get(primitivePropIndex));
                fillTableCell(table, rowIndex, column, hours, fontSize,
                        TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
            }
        }
        // technical row
        fillTableCell(table, rowIndex, 0, "в том числе:", fontSize,
                TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.ZERO);
        rowIndex++;
        // classroom activities rows
        Set<String> activityNames = presentActivities.keySet();
        for (String name : activityNames) {
            String text = translationMap.get(name);
            fillTableCell(table, rowIndex, 0, text, fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.HALF_TAB);
            for (int column = 1; column < numberOfColumns; column++) {
                Term term = terms.get(column - 1);
                String hours = String.valueOf(term.getClassroomActivities().get(name));
                fillTableCell(table, rowIndex, column, hours, fontSize,
                        TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
            }
            rowIndex++;
        }
        // leftover rows
        for (int j = numberOfFirstComingProperties; j < listOfFields.size(); j++, rowIndex++, primitivePropIndex++) {
            String text = translationMap.get(listOfFields.get(j).getName());
            if (text == null) {
                continue; // in case HashMap<String, Integer> field is placed after all other fields in Term.class
            }
            fillTableCell(table, rowIndex, 0, text, fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.ZERO);
            for (int column = 1; column < numberOfColumns; column++) {
                Term term = terms.get(column - 1);
                String hours = String.valueOf(term.getPrimitiveFieldData().get(primitivePropIndex));
                fillTableCell(table, rowIndex, column, hours, fontSize,
                        TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
            }
        }
    }


    /**
     * Fills out competence table by given table number in the document.
     * @param document document to work with
     * @param tableNumber table sequence number in the document (6)
     */
    public void fillOutCompetencesTable(XWPFDocument document, int tableNumber, int fontSize, RPD rpdToDraft) {
        List<XWPFTable> documentTables = document.getTables();
        if (documentTables.isEmpty() || documentTables.size() <= tableNumber) {
            return;
        }
        XWPFTable competencesTable = documentTables.get(tableNumber); // 6
        competencesTable.removeRow(1);

        List<Competence> competences = rpdToDraft.getCompetences();
        competences.forEach(competence -> {
            XWPFTableRow row = competencesTable.createRow();

            XWPFRun firstCellRun = row.getCell(0).getParagraphArray(0).createRun();
            row.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            firstCellRun.setFontSize(fontSize);
            firstCellRun.setText(competence.getCategoryName(), 0);

            XWPFRun secondCellRun = row.getCell(1).getParagraphArray(0).createRun();
            row.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            secondCellRun.setFontSize(fontSize);
            secondCellRun.setText(competence.getName() + " " + competence.getDescription(), 0);

            XWPFRun thirdCellRun = row.getCell(2).getParagraphArray(0).createRun();
            row.getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            thirdCellRun.setFontSize(fontSize);
            StringBuilder achievementIndicatorsText = new StringBuilder();
            for (AchievementIndicator indicator : competence.getAchievementIndicators()) {
                achievementIndicatorsText.append(indicator.getCode());
                achievementIndicatorsText.append(" ");
                achievementIndicatorsText.append(indicator.getDescription());
                achievementIndicatorsText.append('\n');
            }
            outputByLineInTable(row.getCell(2).getParagraphArray(0), achievementIndicatorsText.toString(), FirstLineIndent.ZERO);
        });

        competencesTable.setTableAlignment(TableRowAlign.RIGHT);
    }

    /**
     * Fills out attestation table based on discipline attestation type.
     * @param document document to work with
     * @param tableNumber table index in the document
     */
    public void fillOutAttestationTable(XWPFDocument document, int tableNumber, RPD rpdToDraft) {
        if (document.getTables().isEmpty() || document.getTables().size() <= tableNumber) {
            return;
        }
        XWPFTable table = document.getTables().get(tableNumber);
        table.removeRow(1);
        table.createRow();
        String attestation = "";
        String description = "";
        if (rpdToDraft.isExamAttestationType()) {
            attestation = "Экзамен";
            description = "Список вопросов к экзамену\nЗадачи\nТесты\n";
        } else if (rpdToDraft.isTestAttestationType()) {
            attestation = "Зачет";
            description = "Список вопросов к зачету\nЗадачи\nТесты";
        } else if (rpdToDraft.isDiffTestAttestationType()) {
            attestation = "Дифференцированный зачет";
            description = "Список вопросов к дифференцированному зачету\nЗадачи\nТесты\n";
        }
        fillTableCell(table, 1, 0, attestation, fontSize,
                TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.ZERO);
        fillTableCellFormatted(table, 1, 1, description, fontSize,
                ParagraphAlignment.LEFT);
        if (rpdToDraft.isHasCoursework()) {
            table.createRow();
            fillTableCell(table, 2, 0, "Выполнение курсовой работы", fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.ZERO);
            fillTableCell(table, 2, 1, "Экспертная оценка на основе требований к содержанию курсовой работы по дисциплине.", fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.ZERO);
        }
    }

    /**
     * Inserts given image into the cell of the given table by coordinates.
     * @param table table to work with
     * @param base64String base64 string with encoded .png image
     * @param rowIndex row index of the cell to insert image to
     * @param columIndex column index of the cell to insert image to
     */
    private void insertImagesIntoTableCell(XWPFTable table, String base64String,
                                           int rowIndex, int columIndex) {
        XWPFTableRow row = table.getRow(rowIndex);
        XWPFTableCell cell = row.getCell(columIndex);
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun imageRun = paragraph.createRun();
        byte[] bytes = Base64.getDecoder().decode(base64String.split(",")[1]);
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(byteStream);
            int width = image.getWidth();
            int height = image.getHeight();
            double widthRatio = (double) 200 / width;
            double heightRatio = (double) 100 / height;
            double scalingFactor = Math.min(widthRatio, heightRatio);
            int scaledWidth = (int) (width * scalingFactor);
            int scaledHeight = (int) (height * scalingFactor);
            InputStream imageStream = new ByteArrayInputStream(bytes);
            imageRun.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_PNG, "",
                    Units.toEMU(scaledWidth), Units.toEMU(scaledHeight));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds columns for solo study hours table based on number of terms in target discipline.
     * @param document document to work with
     * @param soloTableIndex table index in the document
     * @param rpdToDraft rpd object for the discipline
     */
    public void expandTermDependentTable(XWPFDocument document, int soloTableIndex, RPD rpdToDraft) {
        // solo study table expansion
        XWPFTable table = document.getTables().get(soloTableIndex);
        table.removeRow(1);
        List<Term> terms = rpdToDraft.getTerms();
        terms.remove(0);
        // Add columns
        int numberOfTerms = terms.size();
        for (int j = 0; j < numberOfTerms - 1; j++) {
            table.addNewCol();
            // The method behavior is unknown (but works properly)
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(2000));
        }
        for (int j = 0; j < terms.size(); j++) {
            String text = "Семестр" + terms.get(j).getNumber();
            fillTableCell(table, 0, j + 2, text, fontSize,
                    TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
        }
    }

    /**
     * Fills out generic tables based on TableDTO's from teacher input
     * @param document - the document to work with
     * @param startIndex - index generic tables start to appear from
     */
    private void fillOutGenericTables(XWPFDocument document, int startIndex) {
        List<TableDTO> tableDTOS = rpdToDraft.getTables().stream().map(TableDataDTO::getBody).toList();
        List<XWPFTable> documentTables = document.getTables();
        for (int j = 0; j < tableDTOS.size(); j++) {
            TableDTO dto = tableDTOS.get(j);
            if (dto == null) {
                continue;
            }
            XWPFTable table = documentTables.get(j + startIndex);
            if (j == firstAssessmentTableIndex - startIndex + 1 || j == firstAssessmentTableIndex - startIndex) {
                continue;
            }
            if (dto.getRows() == null || dto.getRows().size() == 0) {
                fillTableCell(table, 1, 0, translationMap.get(defaultPlaceholder), fontSize,
                        TableWidthType.AUTO, ParagraphAlignment.LEFT, FirstLineIndent.ZERO);
                continue;
            }
            table.removeRow(1);
            int columnIndexOffset = 0;
            if (dto.isRowNumbering()) {
                columnIndexOffset = 1;
            }
            int rowIndexOffset = 0;
            if (dto.isColumnNumbering()) {
                rowIndexOffset = 1;
            }
            List<List<String>> rows = dto.getRows();
            for (int r = 0; r < rows.size(); r++) {
                table.createRow();
            }
            int rowLength = rows.get(0).size();
            for (List<String> row : rows) {
                if (rowLength < row.size()) {
                    rowLength = row.size();
                }
            }
            if (dto.isColumnNumbering()) {
                table.createRow();
                if (rowLength == 1) {
                    fillTableCell(table, 1, 1, "2", fontSize,
                            TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
                } else {
                    for (int column = 0; column < rowLength; column++) {
                        String text = String.valueOf(column + 1);
                        fillTableCell(table, 1, column, text, fontSize,
                                TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
                    }
                }
            }
            List<Integer> headerRowsIndexes = new ArrayList<>();
            if (dto.getInRowHeaders() != null) {
                headerRowsIndexes = Arrays.stream(dto.getInRowHeaders()).map(InRowHeaderDTO::getRow).toList();
            }
            List<Integer> sumsRowsIndexes = new ArrayList<>();
            if (dto.getCheckSums() != null && dto.getCheckSums().length != 0) {
                sumsRowsIndexes.addAll(Arrays.stream(dto.getCheckSums()).map(CheckSumDTO::getRow).toList());
            }
            if (dto.getTotalSum() != null) {
                sumsRowsIndexes.add(dto.getTotalSum().getRow());
            }
            if (dto.isRowNumbering()) {
                for (int row = 1, number = 1; row <= rows.size() + rowIndexOffset; row++, number++) {
                    if (headerRowsIndexes.contains(row) || sumsRowsIndexes.contains(row)) {
                        number--;
                        continue;
                    }
                    fillTableCell(table, row, 0, String.valueOf(number), fontSize,
                            TableWidthType.AUTO, ParagraphAlignment.CENTER, FirstLineIndent.ZERO);
                }
            }
            for (int row = rowIndexOffset, dtoIndex = 0; row < rows.size() + rowIndexOffset; row++, dtoIndex++) {
                List<String> dtoRow = rows.get(dtoIndex);
                for (int column = 0; column < dtoRow.size(); column++) {
                    ParagraphAlignment alignment = ParagraphAlignment.CENTER;
                    if (dtoRow.size() == 1 && rowLength != 1) {
                        alignment = ParagraphAlignment.LEFT;
                        try {
                            mergeCellHorizontally(table, row + 1, columnIndexOffset, rowLength - 1);
                        } catch (Exception ex) {
                            System.out.println("Failed to merge cells: " + ex.getMessage());
                        }
                    }
                    String text = dtoRow.get(column);
                    if (dtoRow.size() > 1) {
                        if (column == 0) {
                            alignment = ParagraphAlignment.LEFT;
                        }
                    }
                    if (headerRowsIndexes.contains(row + 1)) {
                        alignment = ParagraphAlignment.CENTER;
                    }
                    fillTableCellFormatted(table, row + 1, column + columnIndexOffset, text, fontSize, alignment);
                }
            }
            // Image handling
            ImageDTO[] imageDtoS = dto.getImages();
            if (imageDtoS != null && imageDtoS.length != 0) {
                List<ImageDTO> images = Arrays.stream(imageDtoS).toList();
                for (ImageDTO image : images) {
                    int cell = image.getCol();
                    if (dto.isRowNumbering()) {
                        cell += 1;
                    }
                    insertImagesIntoTableCell(table, image.getBytes(), image.getRow() + 1, cell);
                }
            }
        }
    }

    /**
     * Merges cells in row of a given table.
     * @param table target table
     * @param rowIndex rowIndex contents cells to merge
     * @param fromCol start column to merge
     * @param toCol last cell to merge
     */
    private void mergeCellHorizontally(XWPFTable table, int rowIndex, int fromCol, int toCol) {
        XWPFTableCell cell = table.getRow(rowIndex).getCell(fromCol);
        // Some black magic below
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if (tcPr == null) {
            tcPr = cell.getCTTc().addNewTcPr();
        }
        if (tcPr.isSetGridSpan()) {
            tcPr.getGridSpan().setVal(BigInteger.valueOf(toCol-fromCol+1));
        } else {
            tcPr.addNewGridSpan().setVal(BigInteger.valueOf(toCol-fromCol+1));
        }
        // Extra cells must be removed
        int mergedCellNum = toCol - fromCol;
        int lastCellInRowIndex = table.getRow(rowIndex).getTableCells().size() - 1;
        for (int j = lastCellInRowIndex; j > lastCellInRowIndex - mergedCellNum; j--) {
            table.getRow(rowIndex).getCtRow().removeTc(j);
        }
    }

    /**
     * Sets text in table cell.
     * @param table table to fill out a cell in
     * @param rowIndex index of table row contents the cell
     * @param collIndex index of the cell in the row
     * @param text text to print into the cell
     * @param fontSize font size properties of the text
     * @param widthType the cell width type
     * @param alignment alignment of the text in the cell
     * @param leftIndentType indent of the text in cell
     */
    private void fillTableCell(XWPFTable table, int rowIndex, int collIndex, String text, double fontSize,
                               TableWidthType widthType, ParagraphAlignment alignment, FirstLineIndent leftIndentType) {
        XWPFTableCell cell = table.getRow(rowIndex).getCell(collIndex);
        cell.setWidthType(widthType);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        XWPFParagraph paragraph = cell.getParagraphArray(0);
        paragraph.setAlignment(alignment);
        int indent = switch (leftIndentType) {
            case ZERO -> 0;
            case TAB -> 720;
            case HALF_TAB -> 360;
        };
        paragraph.setIndentationLeft(indent);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(fontSize);
        run.setText(text);
    }

    /**
     * Sets text in table cell with multi-line or bullet list output support.
     * @param table - table to work with
     * @param rowIndex - cell's row index
     * @param columnIndex - cell's column index
     * @param text - text to be placed in the cell (with '\n' or '--' special symbols)
     * @param fontSize - text font size
     * @param paragraphAlignment - paragraph alignment (is LEFT for non-plain output)
     */
    private void fillTableCellFormatted(XWPFTable table, int rowIndex, int columnIndex, String text, int fontSize, ParagraphAlignment paragraphAlignment) {
        XWPFTableCell cell = table.getRow(rowIndex).getCell(columnIndex);
        if (cell == null) {
            System.out.println("Cell imput failed: " + rowIndex + " " + columnIndex);
            return;
        }
        cell.setWidthType(TableWidthType.AUTO);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        XWPFParagraph paragraph = cell.getParagraphArray(0);
        paragraph.createRun().setFontSize(fontSize);
        if (text.contains("\n")) {
            outputByLineInTable(paragraph, text, FirstLineIndent.ZERO);
        } else if (text.contains("--")) {
            outputAsBulletList(paragraph, text);
        } else {
            fillTableCell(table, rowIndex, columnIndex, text, fontSize,
                    TableWidthType.AUTO, paragraphAlignment , FirstLineIndent.ZERO);
        }
    }


    /**
     * Replaces recognized placeholders with values in every document paragraph.
     * The placeholders and their values are defined in HashMap structure in a class filed
     * @param document the XWPF document to replace placeholders in
     */
    private void replaceTextInParagraphs(XWPFDocument document) {
        List<XWPFParagraph> paras = document.getParagraphs();
        List<XWPFParagraph> paragraphs = new LinkedList<>(paras);
        // List iterator needed to avoid ConcurrentModificationException (when concurrent modification is needed)
        ListIterator<XWPFParagraph> paragraphIterator = paragraphs.listIterator();
        while(paragraphIterator.hasNext()) {
            XWPFParagraph currentParagraph = paragraphIterator.next();
            List<XWPFRun> runList = currentParagraph.getRuns();
            if (runList.isEmpty()) {
                continue;
            }
            processRuns(currentParagraph, runList);
        }
    }

    /**
     * Replaces all recognized placeholders in tables of the given document with values.
     * @param document the XWPF document to replace placeholders in
     */
    private void replaceTextInTables(XWPFDocument document) {
        List<XWPFTable> documentsTables = document.getTables();
        if (documentsTables.isEmpty()) {
            return;
        }
        documentsTables.forEach(table -> {
            List<XWPFTableRow> rows = table.getRows();
            rows.forEach(row -> {
                List<XWPFTableCell> cells = row.getTableCells();
                cells.forEach(cell -> {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    paragraphs.forEach(paragraph -> {
                        List<XWPFRun> runList = paragraph.getRuns();
                        processRuns(paragraph, runList);
                    });
                });
            });
        });
    }

    /**
     * Iterates over runs in currentParagraph, combines them in one,
     * replaces variable placeholders with their values from given source.
     * The method changes currentParagraph run structure: removing and adding runs,
     * saving their content.
     * @param currentParagraph currentParagraph that contents the runs
     * @param runList run list to iterate over
     */
    private void processRuns(XWPFParagraph currentParagraph, List<XWPFRun> runList) {
        int runListSize = runList.size();
        String runText = currentParagraph.getText();
        int varInRunNum = runText.split("\\$").length - 1;

        for (int i = 0; i < varInRunNum; i++) {
            int firstRunWithVarIndex = -1;
            int lastRunWithVarIndex = runListSize;
            for (XWPFRun run : runList) {
                firstRunWithVarIndex++;
                if (run.text().contains("${")) {
                    String combinedText = "";
                    String text = "";
                    for (int j = firstRunWithVarIndex; j < runListSize; j++) {
                        text = runList.get(j).text();
                        combinedText += text;
                        if (text.contains("}")) {
                            lastRunWithVarIndex = j;
                            break;
                        }
                    }

                    int startOfVar = combinedText.indexOf("$");
                    int endOfVar = combinedText.indexOf("}");

                    String variable = combinedText.substring(startOfVar + 2, endOfVar);
                    String replacement = getWithReplacedValue(variable);
                    String runFinalText = combinedText.substring(0, startOfVar)
                            + replacement
                            + combinedText.substring(endOfVar + 1);

                    if (firstRunWithVarIndex == 0) {
                        for (int j = 0; j < lastRunWithVarIndex; j++) {
                            currentParagraph.removeRun(0);
                        }
                        if (requiredBulletListOutput(variable)) {
                            outputAsBulletList(currentParagraph, runFinalText);
                            break;
                        }
                        if (requiredMultiLineOutput(variable)) {
                            outputByLine(currentParagraph, runFinalText, FirstLineIndent.TAB);
                            break;
                        }
                        runList.get(0).setText(runFinalText, 0);
                        runList.get(0).setFontSize(12);
                        currentParagraph.setFirstLineIndent(720);
                    } else {
                        for (int j = firstRunWithVarIndex; j < runListSize - 1 && j < lastRunWithVarIndex; j++) {
                            currentParagraph.removeRun(firstRunWithVarIndex);
                        }
                        runList.get(firstRunWithVarIndex).setText(runFinalText, 0);
                        runList.get(firstRunWithVarIndex).setFontSize(12);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Creates bullet list in-place of the targetParagraph with creation of paragraph for each line in runFinalText,
     * where lines are separated by '\n' symbol.
     * @param targetParagraph paragraph to be replaced with bullet list
     * @param runFinalText all the text that bullet list shall content
     */
    private void outputAsBulletList(XWPFParagraph targetParagraph, String runFinalText) {
        XWPFDocument document = targetParagraph.getDocument();
        XmlCursor cursor = targetParagraph.getCTP().newCursor();

        String[] lines = runFinalText.split("\n");
        int bulletListSize = lines.length;

        XWPFParagraph newParagraph;
        XWPFRun newRun;
        XmlCursor helperCursor;

        BigInteger id = getNewDecimalNumberingId(document, BigInteger.valueOf(1));

        for (int j = 0; j < bulletListSize; j++) {
            newParagraph = document.createParagraph();
            newParagraph.getCTP().setPPr(targetParagraph.getCTP().getPPr());
            newParagraph.setNumID(id);

            newRun = newParagraph.createRun();
            newRun.getCTR().setRPr(targetParagraph.getRuns().get(0).getCTR().getRPr());
            newRun.setText(lines[j], 0);
            newRun.setFontSize(fontSize);

            helperCursor = newParagraph.getCTP().newCursor();
            helperCursor.moveXml(cursor);
            helperCursor.dispose();
        }

        targetParagraph.removeRun(0);
    }

    /**
     * Finds available id for numeration in the given document.
     * @param document document where to find id
     * @param abstractNumId unique number of the bullet list being created
     * @return found id for the new list
     */
    private BigInteger getNewDecimalNumberingId(XWPFDocument document, BigInteger abstractNumId) {
        CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
        cTAbstractNum.setAbstractNumId(abstractNumId.add(BigInteger.ONE));

        CTLvl cTLvl = cTAbstractNum.addNewLvl();
        cTLvl.setIlvl(BigInteger.ZERO);
        cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
        cTLvl.addNewLvlText().setVal("—");
        cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

        XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
        XWPFNumbering numbering = document.createNumbering();
        abstractNumId = numbering.addAbstractNum(abstractNum);

        return numbering.addNum(abstractNumId);
    }

    /**
     * Creates new paragraphs for each line of output given as text with '\n' symbols as delimiters.
     * Styles and font family copied from the original paragraph.
     * First lines have tabulation in the beginning. NB: leaves empty line after output
     * @param currentParagraph paragraph that contents the run
     * @param runFinalText all the text the new paragraphs shall content
     */
    private void outputByLine(XWPFParagraph currentParagraph, String runFinalText, FirstLineIndent indent) {
        String[] lines = runFinalText.split("\n");

        int firstLineIndent = switch (indent) {
            case ZERO -> 0;
            case TAB -> 720;
            case HALF_TAB -> 360;
            default -> 720;
        };

        XWPFDocument document = currentParagraph.getDocument();
        XmlCursor cursor = currentParagraph.getCTP().newCursor();

        XWPFParagraph newParagraph;
        XWPFRun newRun;
        XmlCursor helperCursor;

        for (String line : lines) {
            newParagraph = document.createParagraph();
            newParagraph.getCTP().setPPr(currentParagraph.getCTP().getPPr());
            newParagraph.setFirstLineIndent(firstLineIndent);

            newRun = newParagraph.createRun();
            newRun.getCTR().setRPr(currentParagraph.getRuns().get(0).getCTR().getRPr());
            newRun.setText(line, 0);
            newRun.setFontSize(fontSize);

            helperCursor = newParagraph.getCTP().newCursor();
            helperCursor.moveXml(cursor);
            helperCursor.dispose();
        }

        currentParagraph.removeRun(0);
    }

    /**
     * Creates multiline output inside one given paragraph with runs. Used for tables only.
     * @param currentParagraph paragraph of target table cell to put lines into
     * @param runFinalText list of runs with text
     * @param indent first line indent for the text
     */
    private void outputByLineInTable(XWPFParagraph currentParagraph, String runFinalText, FirstLineIndent indent) {
        String[] lines = runFinalText.split("\n");

        int firstLineIndent = switch (indent) {
            case ZERO -> 0;
            case TAB -> 720;
            case HALF_TAB -> 360;
            default -> 720;
        };

        // Remove existing runs to start fresh
        int numRuns = currentParagraph.getRuns().size();
        for (int i = numRuns - 1; i >= 0; i--) {
            currentParagraph.removeRun(i);
        }

        // Set paragraph properties
        currentParagraph.setIndentationLeft(firstLineIndent);

        // Add each line as a separate run
        for (int i = 0; i < lines.length; i++) {
            XWPFRun newRun = currentParagraph.createRun();

            newRun.setText(lines[i]);
            newRun.setFontSize(fontSize);

            // Add a break after each line except the last one
            if (i < lines.length - 1) {
                newRun.addBreak();
            }
        }
    }

    /**
     * Checks variable placeholder syntax for the symbols that indicate multiline output requirement.
     * @param placeholder variable placeholder
     * @return weather the given value requires multiline output
     */
    private boolean requiredMultiLineOutput(String placeholder) {
        return placeholder.contains("<") && placeholder.contains(">");
    }

    /**
     * Checks variable placeholder syntax for the symbols that indicate bullet list output requirement.
     * @param placeholder variable placeholder
     * @return weather the given variable requires bullet list output
     */
    private boolean requiredBulletListOutput(String placeholder) {
        return placeholder.contains("-");
    }

    /**
     * Gets replacement for the given variable from the placeholderMap field. Saves brackets and output special symbols.
     * @param variable variable in brackets: {-name-} or {<name>} or {name}
     * @return placeholder value in origin brackets
     */
    private String getWithReplacedValue(String variable) {
        String pureName;
        if (requiredBulletListOutput(variable) || requiredMultiLineOutput(variable)) {
            pureName = variable.substring(1, variable.length() - 1);
        } else {
            pureName = variable;
        }
        if (placeholdersMap.get(pureName) == null || Objects.equals(placeholdersMap.get(pureName), "null")) {
            return translationMap.get(defaultPlaceholder);
        } else {
            return placeholdersMap.get(pureName);
        }
    }
}
