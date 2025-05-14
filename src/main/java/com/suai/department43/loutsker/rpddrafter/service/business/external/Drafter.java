package com.suai.department43.loutsker.rpddrafter.service.business.external;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.RPDEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.RPD;
import com.suai.department43.loutsker.rpddrafter.domain.payload.FileDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.TeacherInputDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface Drafter {
    FileDTO getDraftedDocument(long teacherId, long disciplineId, TeacherInputDTO input);
    FileDTO getReDraftedDocument(long teacherId, long rpdId, TeacherInputDTO input);
    RPDEntity getImportData(long rpdId);
    List<RPDEntity> getRPDVersionsByProperties(String disciplineName, String programCode, int enrollYear, String authorName);
    List<RPDEntity> getAllRPDs();
    RPDEntity getRPDById(long id);
    RPDEntity getTemplateMergedTables(long selectedDisciplineId, long selectedRPDToImportId);
    void deleteRPDById(long id);
}
