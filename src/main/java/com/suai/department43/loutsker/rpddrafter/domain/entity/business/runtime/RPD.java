package com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.suai.department43.loutsker.rpddrafter.domain.payload.TableDTO;
import com.suai.department43.loutsker.rpddrafter.domain.payload.TableDataDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RPD implements Serializable {
    private boolean fullEducationFormat;
    private boolean eveningEducationFormat;
    private boolean extramuralEducationFormat;

    private boolean masterDegree;
    private boolean bachelorDegree;
    private boolean specialistDegree;

    private boolean examAttestationType;
    private boolean testAttestationType;
    private boolean diffTestAttestationType;

    private boolean hasCoursework;

    private HashMap<String, String> placeholders = new HashMap<>();
    private List<TableDataDTO> tables = new ArrayList<>();

    private List<Competence> competences = new ArrayList<>();
    private List<Term> terms = new ArrayList<>();
    private Term totalTerm;

    public RPD() {
    }

    public boolean isFullEducationFormat() {
        return fullEducationFormat;
    }

    public void setFullEducationFormat(boolean fullEducationFormat) {
        this.fullEducationFormat = fullEducationFormat;
    }

    public boolean isEveningEducationFormat() {
        return eveningEducationFormat;
    }

    public void setEveningEducationFormat(boolean eveningEducationFormat) {
        this.eveningEducationFormat = eveningEducationFormat;
    }

    public boolean isExtramuralEducationFormat() {
        return extramuralEducationFormat;
    }

    public void setExtramuralEducationFormat(boolean extramuralEducationFormat) {
        this.extramuralEducationFormat = extramuralEducationFormat;
    }

    public boolean isMasterDegree() {
        return masterDegree;
    }

    public void setMasterDegree(boolean masterDegree) {
        this.masterDegree = masterDegree;
    }

    public boolean isBachelorDegree() {
        return bachelorDegree;
    }

    public void setBachelorDegree(boolean bachelorDegree) {
        this.bachelorDegree = bachelorDegree;
    }

    public boolean isSpecialistDegree() {
        return specialistDegree;
    }

    public void setSpecialistDegree(boolean specialistDegree) {
        this.specialistDegree = specialistDegree;
    }

    public boolean isExamAttestationType() {
        return examAttestationType;
    }

    public void setExamAttestationType(boolean examAttestationType) {
        this.examAttestationType = examAttestationType;
    }

    public boolean isTestAttestationType() {
        return testAttestationType;
    }

    public void setTestAttestationType(boolean testAttestationType) {
        this.testAttestationType = testAttestationType;
    }

    public boolean isDiffTestAttestationType() {
        return diffTestAttestationType;
    }

    public void setDiffTestAttestationType(boolean diffTestAttestationType) {
        this.diffTestAttestationType = diffTestAttestationType;
    }

    public boolean isHasCoursework() {
        return hasCoursework;
    }

    public void setHasCoursework(boolean hasCoursework) {
        this.hasCoursework = hasCoursework;
    }

    public HashMap<String, String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(HashMap<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public List<TableDataDTO> getTables() {
        return tables;
    }

    public void setTables(List<TableDataDTO> tables) {
        this.tables = tables;
    }

    public List<Competence> getCompetences() {
        return competences;
    }

    public void setCompetences(List<Competence> competences) {
        this.competences = competences;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public Term getTotalTerm() {
        return totalTerm;
    }

    public void setTotalTerm(Term totalTerm) {
        this.totalTerm = totalTerm;
    }
}
