package com.suai.department43.loutsker.rpddrafter.service.business.external;

import com.suai.department43.loutsker.rpddrafter.domain.payload.DisciplineEssentialsDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.TableDataDTO;

import java.util.HashMap;
import java.util.List;

public interface Technician {
    DisciplineEssentialsDTO getDisciplineEssentials(long disciplineId);
    List<String> getTemplateInputPlaceholderNames();
    HashMap<String, String> getSamples();
    HashMap<String, String> getTranslationMap();
    HashMap<String, String> getSamplesMap();
    List<TableDataDTO> getRPDTablesForDiscipline(long disciplineId);
    int getFontSize();
    int getFirstDisciplineContentTableIndex();
    int getFirstAssessmentTableIndex();
}
