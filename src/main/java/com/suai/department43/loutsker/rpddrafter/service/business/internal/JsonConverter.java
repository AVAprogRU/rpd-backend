package com.suai.department43.loutsker.rpddrafter.service.business.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.DisciplineEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Teacher;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.AchievementIndicator;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Discipline;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.RPD;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Term;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.UniversityPosition;
import com.suai.department43.loutsker.rpddrafter.domain.payload.*;
import com.suai.department43.loutsker.rpddrafter.exception.business.DraftFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class JsonConverter {
    ObjectMapper mapper;

    @Autowired
    public JsonConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void merge(RPD rpd, Discipline discipline) {
        rpd.setFullEducationFormat(discipline.isFullTimeEducationFormat());
        rpd.setEveningEducationFormat(discipline.isEveningEducationFormat());
        rpd.setExtramuralEducationFormat(discipline.isExtramuralEducationFormat());

        rpd.setMasterDegree(discipline.isMasterDegree());
        rpd.setBachelorDegree(discipline.isBachelorDegree());
        rpd.setSpecialistDegree(discipline.isSpecialistDegree());

        rpd.setExamAttestationType(discipline.isExamAttestationType());
        rpd.setTestAttestationType(discipline.isTestAttestationType());
        rpd.setDiffTestAttestationType(discipline.isDiffTestAttestationType());

        rpd.setHasCoursework(discipline.isHasCoursework());

        rpd.setTotalTerm(discipline.getTermTotal());
        rpd.setTerms(discipline.getTerms());
        rpd.setCompetences(discipline.getCompetences());

        rpd.setPlaceholders(collectFields(discipline));
    }

    public void mergeImport(RPD rpd, Discipline discipline, Set<String> autoFilledPlaceholdersMap) throws JsonProcessingException {
        rpd.setFullEducationFormat(discipline.isFullTimeEducationFormat());
        rpd.setEveningEducationFormat(discipline.isEveningEducationFormat());
        rpd.setExtramuralEducationFormat(discipline.isExtramuralEducationFormat());

        rpd.setMasterDegree(discipline.isMasterDegree());
        rpd.setBachelorDegree(discipline.isBachelorDegree());
        rpd.setSpecialistDegree(discipline.isSpecialistDegree());

        rpd.setExamAttestationType(discipline.isExamAttestationType());
        rpd.setTestAttestationType(discipline.isTestAttestationType());
        rpd.setDiffTestAttestationType(discipline.isDiffTestAttestationType());

        rpd.setHasCoursework(discipline.isHasCoursework());

        rpd.setTotalTerm(discipline.getTermTotal());
        rpd.setTerms(discipline.getTerms());
        rpd.setCompetences(discipline.getCompetences());

        HashMap<String, String> newPlaceholders = collectFields(discipline);
        HashMap<String, String> oldPlaceholders = rpd.getPlaceholders();  // Предполагаем, что в RPD есть геттер

        List<String> changedKeys = new ArrayList<>();

        System.out.println("Old placeholders: " + mapper.writeValueAsString(oldPlaceholders));
        System.out.println("New placeholders: " + mapper.writeValueAsString(newPlaceholders));

        for (Map.Entry<String, String> entry : newPlaceholders.entrySet()) {
            String key = entry.getKey();
            String newValue = entry.getValue();
            String oldValue = oldPlaceholders.get(key);

            if (oldValue == null || !Objects.equals(oldValue, newValue)) {
                oldPlaceholders.put(key, newValue);
                changedKeys.add(key);
                System.out.println("Updated key: " + key + ", New value: " + newValue);
            }
        }
        System.out.println("Old placeholders: " + mapper.writeValueAsString(oldPlaceholders));
        rpd.setPlaceholders(oldPlaceholders);  // Обновленные данные
        rpd.getChangedFields().addAll(changedKeys);  // Сохраняем измененные ключи
    }

    public List<Discipline> extractDisciplineDTOsFromUMODTO(UMODTO umodto) {
        List<Discipline> disciplines = umodto.getDisciplines();
        disciplines.forEach(discipline -> {
            discipline.setInstitution(umodto.getInstitution());
            discipline.setInstitutionName(umodto.getInstitutionName());
            discipline.setMinistryOfEducation(umodto.getMinistryOfEducation());
            discipline.setEnrollYear(umodto.getEnrollYear());
        });
        return disciplines;
    }

    public DisciplineEssentialsDTO extractEssentials(Discipline discipline, HashMap<String, String> translationMap) {
        DisciplineEssentialsDTO dto = new DisciplineEssentialsDTO();

        dto.setFullEducationFormat(discipline.isFullTimeEducationFormat());
        dto.setExtramuralEducationFormat(discipline.isExtramuralEducationFormat());
        dto.setEveningEducationFormat(discipline.isEveningEducationFormat());

        dto.setBachelorDegree(discipline.isBachelorDegree());
        dto.setMasterDegree(discipline.isMasterDegree());
        dto.setSpecialistDegree(discipline.isSpecialistDegree());

        dto.setExamAttestationType(discipline.isExamAttestationType());
        dto.setTestAttestationType(discipline.isTestAttestationType());
        dto.setDiffTestAttestationType(discipline.isDiffTestAttestationType());

        dto.setHasCoursework(discipline.isHasCoursework());

        dto.setEnrollYear(discipline.getEnrollYear());
        dto.setDisciplineName(discipline.getDisciplineName());
        dto.setProgramCode(discipline.getProgramCode());

        dto.setTerms(discipline.getTerms());

        List<AchievementIndicator> indicators = new ArrayList<>();
        discipline.getCompetences().forEach(competence -> {
            indicators.addAll(competence.getAchievementIndicators());
        });
        dto.setAchievementsIndicators(indicators);

        return dto;
    }

    public List<DisciplineEntity> extractDisciplineFromDisciplineDto(List<Discipline> dtos) {
        List<DisciplineEntity> disciplineEntities = new ArrayList<>();
        dtos.forEach(dto -> {
            DisciplineEntity disciplineEntity = new DisciplineEntity(dto.getDisciplineName());
            disciplineEntity.setBody(dto);
            disciplineEntities.add(disciplineEntity);
        });
        return disciplineEntities;
    }

    public String getTeacherFormattedName(Teacher teacher) {
        return teacher.getLastname() + " "
                + teacher.getName().charAt(0) + ". "
                + teacher.getPatronymic().charAt(0) + ".";
    }

    public String getTeacherFormattedName(FIODTO fio) {
        return fio.getLastname() + " "
                + fio.getName().charAt(0) + ". "
                + fio.getPatronymic().charAt(0) + ".";
    }

    public String getPositionFormattedName(UniversityPosition position) {
        String fullPosition = position.getPosition();
        if (!position.getPosition().isEmpty()) {
            fullPosition += ", ";
        }
        fullPosition += position.getAcademicDegree();
        if (!position.getAcademicDegree().isEmpty()) {
            fullPosition += ", ";
        }
        fullPosition += position.getScientificTitle();
        return fullPosition;
    }

    public HashMap<String, String> collectFields(Discipline discipline) {
        try {
            HashMap<String, String> fieldMap = new HashMap<>();
            Field[] fields = Discipline.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(discipline);
                String fieldName = camelToSnake(field.getName());
                if (value instanceof UniversityPosition) {
                    fieldMap.put(fieldName, getPositionFormattedName((UniversityPosition) value));
                }
                if (value instanceof FIODTO) {
                    fieldMap.put(fieldName, getTeacherFormattedName((FIODTO) value));
                }
                if (value instanceof String) {
                    fieldMap.put(fieldName, (String) value);
                }
                if (value instanceof Integer) {
                    fieldMap.put(fieldName, String.valueOf((Integer) value));
                }
            }
            return fieldMap;
        } catch (Exception ex) {
            throw new DraftFailureException("Enable to collect discipline fields");
        }
    }

    private String camelToSnake(String nameInCamel) {
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < nameInCamel.length(); j++) {
            char currentChar = nameInCamel.charAt(j);
            if (Character.isUpperCase(currentChar)) {
                if (j > 0) {
                    result.append("_");
                }
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }
}
