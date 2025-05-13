package com.suai.department43.loutsker.rpddrafter.domain.payload;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.AchievementIndicator;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Term;

import java.util.List;

public class DisciplineEssentialsDTO {
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

    private int enrollYear;
    private String disciplineName;
    private String programCode;

    private List<Term> terms;
    private List<AchievementIndicator> achievementsIndicators;

    public DisciplineEssentialsDTO() {
    }

    public List<AchievementIndicator> getAchievementsIndicators() {
        return achievementsIndicators;
    }

    public void setAchievementsIndicators(List<AchievementIndicator> achievementsIndicators) {
        this.achievementsIndicators = achievementsIndicators;
    }

    public int getEnrollYear() {
        return enrollYear;
    }

    public void setEnrollYear(int enrollYear) {
        this.enrollYear = enrollYear;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
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

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }
}
