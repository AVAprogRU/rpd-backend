package com.suai.department43.loutsker.rpddrafter.service.business.external;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.DisciplineEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Teacher;

import java.util.List;

public interface DepartmentManager {
    List<DisciplineEntity> getDisciplines();
    List<Teacher> getTeachers();
    void assignDisciplineOnTeacher(long teacherId, long disciplineId, String note);
}
