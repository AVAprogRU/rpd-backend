package com.suai.department43.loutsker.rpddrafter.repository.business;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Teacher;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
