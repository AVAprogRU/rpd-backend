package com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teacher")
public class Teacher extends Person {
    @OneToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private UniversityPosition position;
    @OneToMany
    @JoinTable(name = "teacher_discipline",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<DisciplineEntity> assignedDisciplines;

    public Teacher() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher teacher)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(position, teacher.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position);
    }

    public UniversityPosition getPosition() {
        return position;
    }

    public void setPosition(UniversityPosition position) {
        this.position = position;
    }

    public List<DisciplineEntity> getAssignedDisciplines() {
        return assignedDisciplines;
    }

    public void setAssignedDisciplines(List<DisciplineEntity> assignedDisciplineEntities) {
        this.assignedDisciplines = assignedDisciplineEntities;
    }
}
