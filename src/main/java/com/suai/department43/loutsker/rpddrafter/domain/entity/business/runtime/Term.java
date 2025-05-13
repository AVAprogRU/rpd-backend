package com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Term implements RowRepresentable, Serializable {
    private long id;
    private int number;
    private String totalIntensity;
    private int practiceHours;
    private int totalClassroomHours;
    private int soloHours;
    private String intermediateExamType;
    private HashMap<String, Integer> classroomActivities;

    public Term() {
    }

    @JsonIgnore
    @Override
    public List<Object> getPrimitiveFieldData() {
        return List.of(number, totalIntensity, practiceHours, totalClassroomHours, soloHours, intermediateExamType);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTotalIntensity() {
        return totalIntensity;
    }

    public void setTotalIntensity(String totalIntensity) {
        this.totalIntensity = totalIntensity;
    }

    public String getIntermediateExamType() {
        return intermediateExamType;
    }

    public void setIntermediateExamType(String intermediateExamType) {
        this.intermediateExamType = intermediateExamType;
    }

    public int getSoloHours() {
        return soloHours;
    }

    public void setSoloHours(int soloHours) {
        this.soloHours = soloHours;
    }

    public HashMap<String, Integer> getClassroomActivities() {
        return classroomActivities;
    }

    public void setClassroomActivities(HashMap<String, Integer> classroomActivities) {
        this.classroomActivities = classroomActivities;
    }

    public int getPracticeHours() {
        return practiceHours;
    }

    public void setPracticeHours(int practiceHours) {
        this.practiceHours = practiceHours;
    }

    public int getTotalClassroomHours() {
        return totalClassroomHours;
    }

    public void setTotalClassroomHours(int totalClassroomHours) {
        this.totalClassroomHours = totalClassroomHours;
    }
}
