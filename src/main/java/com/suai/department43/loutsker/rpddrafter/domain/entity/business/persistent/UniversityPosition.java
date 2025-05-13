package com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "university_position")
public class UniversityPosition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String position;
    private String academicDegree;
    private String scientificTitle;

    public UniversityPosition() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniversityPosition that)) return false;
        return Objects.equals(position, that.position) && Objects.equals(academicDegree, that.academicDegree) && Objects.equals(scientificTitle, that.scientificTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, academicDegree, scientificTitle);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public String getScientificTitle() {
        return scientificTitle;
    }

    public void setScientificTitle(String scientificTitle) {
        this.scientificTitle = scientificTitle;
    }
}
