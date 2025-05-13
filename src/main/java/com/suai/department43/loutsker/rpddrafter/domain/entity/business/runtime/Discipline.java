package com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.UniversityPosition;
import com.suai.department43.loutsker.rpddrafter.domain.payload.FIODTO;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Discipline implements Serializable {
    private String ministryOfEducation;
    private String institution;
    private String institutionName;

    private int enrollYear;
    private String departmentNumber;

    private UniversityPosition headOfDirectionPosition;
    private FIODTO headOfDirection;

    private String disciplineName;
    private String specializationCode;
    private String specializationName;
    private String direction;

    private boolean fullTimeEducationFormat;
    private boolean eveningEducationFormat;
    private boolean extramuralEducationFormat;

    private boolean masterDegree;
    private boolean bachelorDegree;
    private boolean specialistDegree;

    private boolean examAttestationType;
    private boolean testAttestationType;
    private boolean diffTestAttestationType;

    private UniversityPosition headOfDepartmentPosition;
    private FIODTO headOfDepartment;

    private String programCode;
    private UniversityPosition responsibleForProgramPosition;
    private FIODTO responsibleForProgram;

    private String deputyPositionName;
    private UniversityPosition deputyPosition;
    private FIODTO deputy;

    private List<Competence> competences;

    private String examFormat;
    private String disciplineIntensity;
    private String language;
    private boolean hasCoursework;

    private List<Term> terms;
    private Term termTotal;

    public Discipline() {
    }

    public int getEnrollYear() {
        return enrollYear;
    }

    public void setEnrollYear(int enrollYear) {
        this.enrollYear = enrollYear;
    }

    public String getMinistryOfEducation() {
        return ministryOfEducation;
    }

    public void setMinistryOfEducation(String ministryOfEducation) {
        this.ministryOfEducation = ministryOfEducation;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public UniversityPosition getHeadOfDirectionPosition() {
        return headOfDirectionPosition;
    }

    public void setHeadOfDirectionPosition(UniversityPosition headOfDirectionPosition) {
        this.headOfDirectionPosition = headOfDirectionPosition;
    }

    public FIODTO getHeadOfDirection() {
        return headOfDirection;
    }

    public void setHeadOfDirection(FIODTO headOfDirection) {
        this.headOfDirection = headOfDirection;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getSpecializationCode() {
        return specializationCode;
    }

    public void setSpecializationCode(String specializationCode) {
        this.specializationCode = specializationCode;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isFullTimeEducationFormat() {
        return fullTimeEducationFormat;
    }

    public void setFullTimeEducationFormat(boolean fullTimeEducationFormat) {
        this.fullTimeEducationFormat = fullTimeEducationFormat;
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

    public UniversityPosition getHeadOfDepartmentPosition() {
        return headOfDepartmentPosition;
    }

    public void setHeadOfDepartmentPosition(UniversityPosition headOfDepartmentPosition) {
        this.headOfDepartmentPosition = headOfDepartmentPosition;
    }

    public FIODTO getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(FIODTO headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public UniversityPosition getResponsibleForProgramPosition() {
        return responsibleForProgramPosition;
    }

    public void setResponsibleForProgramPosition(UniversityPosition responsibleForProgramPosition) {
        this.responsibleForProgramPosition = responsibleForProgramPosition;
    }

    public FIODTO getResponsibleForProgram() {
        return responsibleForProgram;
    }

    public void setResponsibleForProgram(FIODTO responsibleForProgram) {
        this.responsibleForProgram = responsibleForProgram;
    }

    public String getDeputyPositionName() {
        return deputyPositionName;
    }

    public void setDeputyPositionName(String deputyPositionName) {
        this.deputyPositionName = deputyPositionName;
    }

    public UniversityPosition getDeputyPosition() {
        return deputyPosition;
    }

    public void setDeputyPosition(UniversityPosition deputyPosition) {
        this.deputyPosition = deputyPosition;
    }

    public FIODTO getDeputy() {
        return deputy;
    }

    public void setDeputy(FIODTO deputy) {
        this.deputy = deputy;
    }

    public List<Competence> getCompetences() {
        return competences;
    }

    public void setCompetences(List<Competence> competences) {
        this.competences = competences;
    }

    public String getExamFormat() {
        return examFormat;
    }

    public void setExamFormat(String examFormat) {
        this.examFormat = examFormat;
    }

    public String getDisciplineIntensity() {
        return disciplineIntensity;
    }

    public void setDisciplineIntensity(String disciplineIntensity) {
        this.disciplineIntensity = disciplineIntensity;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Term getTermTotal() {
        return termTotal;
    }

    public void setTermTotal(Term termTotal) {
        this.termTotal = termTotal;
    }
}
