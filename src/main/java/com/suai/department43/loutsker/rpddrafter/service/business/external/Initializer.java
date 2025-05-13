package com.suai.department43.loutsker.rpddrafter.service.business.external;

import com.suai.department43.loutsker.rpddrafter.domain.payload.FileDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.UMODTO;

import java.util.HashMap;

public interface Initializer {
    void setupTemplate(FileDTO dto);
    void setupTeachersFile(FileDTO dto);
    void setupDisciplinesData(UMODTO umodto);
    void setupTranslationMap(HashMap<String, String> translationMap);
    void setupSamplesMap(HashMap<String, String> samplesMap);
    void setupGenerals(int fontSize, int firstDisciplineContentTableIndex, int firstAssessmentTableIndex);
    FileDTO getTemplateFile();
    FileDTO getTeachersFile();
    FileDTO getDisciplinesFile();
    FileDTO getTranslationsFile();
    FileDTO getSamplesFile();
}
