package com.suai.department43.loutsker.rpddrafter.domain.payload;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Discipline;

import java.io.Serializable;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UMODTO implements Serializable {
    private String ministryOfEducation;
    private String institution;
    private String institutionName;
    private int enrollYear;
    private List<Discipline> disciplines;

    public UMODTO() {}

    public String getMinistryOfEducation() {
        return ministryOfEducation;
    }

    public String getInstitution() {
        return institution;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public int getEnrollYear() {
        return enrollYear;
    }
}
