package com.suai.department43.loutsker.rpddrafter.web.business;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.DisciplineEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Teacher;
import com.suai.department43.loutsker.rpddrafter.service.business.external.DepartmentManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secured/admin")
public class AdminController {
    private final DepartmentManager manager;

    public AdminController(DepartmentManager manager) {
        this.manager = manager;
    }

    @PostMapping("/assign")
    public void assignRPD(@RequestParam long teacherId, @RequestParam long disciplineId, @RequestParam String note) {
        manager.assignDisciplineOnTeacher(teacherId, disciplineId, note);
    }

    @GetMapping("/disciplines")
    public ResponseEntity<List<DisciplineEntity>> getDisciplineList() {
        return ResponseEntity.status(HttpStatus.OK).body(manager.getDisciplines());
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getTeachers() {
        return ResponseEntity.status(HttpStatus.OK).body(manager.getTeachers());
    }
}
