package com.suai.department43.loutsker.rpddrafter.web.business;

import com.suai.department43.loutsker.rpddrafter.domain.payload.FileDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.UMODTO;
import com.suai.department43.loutsker.rpddrafter.service.business.external.Initializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/secured/setup")
public class InitController {
    private final Initializer initializer;

    public InitController(Initializer initializer) {
        this.initializer = initializer;
    }

    @PostMapping("/template")
    public void uploadDocumentTemplate(@RequestBody FileDTO dto) {
        initializer.setupTemplate(dto);
    }

    @PostMapping("/disciplines")
    public void uploadDisciplinesData(@RequestBody UMODTO umodto) {
        initializer.setupDisciplinesData(umodto);
    }

    @PostMapping("/teachers")
    public void uploadTeachersData(@RequestBody FileDTO dto) {
        initializer.setupTeachersFile(dto);
    }

    @PostMapping("/translation")
    public void uploadTranslationMap(@RequestBody HashMap<String, String> translationMap) {
        initializer.setupTranslationMap(translationMap);
    }

    @PostMapping("/general")
    public void uploadGenerals(@RequestParam int fontSize,
                               @RequestParam int firstDisciplineContentTable,
                               @RequestParam int firstAssessmentTableIndex) {
        initializer.setupGenerals(fontSize, firstDisciplineContentTable, firstAssessmentTableIndex);
    }

    @PostMapping("/samples")
    public void uploadSamples(@RequestBody HashMap<String, String> samples) {
        initializer.setupSamplesMap(samples);
    }

    @GetMapping("/documents/teachers")
    public ResponseEntity<FileDTO> getCurrentTeacherFile() {
        return ResponseEntity.status(HttpStatus.OK).body(initializer.getTeachersFile());
    }

    @GetMapping("/documents/template")
    public ResponseEntity<FileDTO> getCurrentTemplateFile() {
        return ResponseEntity.status(HttpStatus.OK).body(initializer.getTemplateFile());
    }

    @GetMapping("/documents/translation")
    public ResponseEntity<FileDTO> getCurrentTranslationFile() {
        return ResponseEntity.status(HttpStatus.OK).body(initializer.getTranslationsFile());
    }

    @GetMapping("/documents/disciplines")
    public ResponseEntity<FileDTO> getCurrentDisciplineFile() {
        return ResponseEntity.status(HttpStatus.OK).body(initializer.getDisciplinesFile());
    }

    @GetMapping("/documents/samples")
    public ResponseEntity<FileDTO> getCurrentSampleFile() {
        return ResponseEntity.status(HttpStatus.OK).body(initializer.getSamplesFile());
    }
}
