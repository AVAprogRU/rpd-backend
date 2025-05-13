package com.suai.department43.loutsker.rpddrafter.web.business;

import com.suai.department43.loutsker.rpddrafter.domain.payload.DisciplineEssentialsDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.TableDataDTO;
import com.suai.department43.loutsker.rpddrafter.service.business.external.Technician;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/secured/technical")
public class TechnicianController {
    private final Technician service;

    public TechnicianController(Technician service) {
        this.service = service;
    }

    @GetMapping("/essentials")
    public ResponseEntity<DisciplineEssentialsDTO> getEssentials(@RequestParam long disciplineId) {
        DisciplineEssentialsDTO essentialsDTO = service.getDisciplineEssentials(disciplineId);
        return ResponseEntity.status(HttpStatus.OK).body(essentialsDTO);
    }

    @GetMapping("/tables/{disciplineId}")
    public ResponseEntity<List<TableDataDTO>> getTemplateTables(@PathVariable long disciplineId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getRPDTablesForDiscipline(disciplineId));
    }

    @GetMapping("/placeholders")
    public ResponseEntity<List<String>> getTemplatePlaceholders() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getTemplateInputPlaceholderNames());
    }

    @GetMapping("/samples")
    public ResponseEntity<HashMap<String, String>> getSamples() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getSamplesMap());
    }

    @GetMapping("/translation")
    public ResponseEntity<HashMap<String, String>> getTranslation() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getTranslationMap());
    }

    @GetMapping("/font")
    public ResponseEntity<Integer> getFontSize() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getFontSize());
    }

    @GetMapping("/index/content")
    public ResponseEntity<Integer> getFirstDisciplineContentTableIndex() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getFirstDisciplineContentTableIndex());
    }

    @GetMapping("/index/assessment")
    public ResponseEntity<Integer> getFirstAssessmentTableIndex() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getFirstAssessmentTableIndex());
    }
}
