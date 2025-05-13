package com.suai.department43.loutsker.rpddrafter.web.business;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.RPDEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.RPD;
import com.suai.department43.loutsker.rpddrafter.domain.payload.FileDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.TeacherInputDTO;
import com.suai.department43.loutsker.rpddrafter.service.business.external.Drafter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("secured/rpd")
public class RPDController {
    private final Drafter service;

    public RPDController(Drafter service) {
        this.service = service;
    }

    @PostMapping("/draft")
    public ResponseEntity<FileDTO> draftRPD(@RequestBody TeacherInputDTO inputDTO,
                                            @RequestParam long disciplineId,
                                            @RequestParam long teacherId) {
        FileDTO fileDTO = service.getDraftedDocument(teacherId, disciplineId, inputDTO);
        return ResponseEntity.status(HttpStatus.OK).body(fileDTO);
    }

    @GetMapping("/import/get")
    public ResponseEntity<RPDEntity> getImport(@RequestParam long rpdId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getImportData(rpdId));
    }

    @PostMapping("/import/draft")
    public ResponseEntity<FileDTO> redraft(@RequestBody TeacherInputDTO inputDTO,
                                           @RequestParam long teacherId,
                                           @RequestParam long rpdId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getReDraftedDocument(teacherId, rpdId, inputDTO));
    }

    @GetMapping("/crud/all")
    public ResponseEntity<List<RPDEntity>> getAllRPD() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllRPDs());
    }

    @GetMapping("/crud/find")
    public ResponseEntity<List<RPDEntity>> getRPDByProperties(@RequestParam String disciplineName,
                                                              @RequestParam String programCode,
                                                              @RequestParam int enrollYear,
                                                              @RequestParam String authorName) {
        List<RPDEntity> rpds = service.getRPDVersionsByProperties(disciplineName, programCode, enrollYear, authorName);
        return ResponseEntity.status(HttpStatus.OK).body(rpds);
    }

    @GetMapping("/crud/get")
    public ResponseEntity<RPDEntity> getRPDById(@RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getRPDById(id));
    }

    @DeleteMapping("/crud/delete")
    public void deleteRPDById(@RequestParam long id) {
        service.deleteRPDById(id);
    }
}
