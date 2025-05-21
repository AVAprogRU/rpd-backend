package com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.RPD;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "rpd")
public class RPDEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String authorName;
    private String authorPosition;
    private String disciplineName;   // по идее это поле не нужно, так как его можно вынуть из сущности дисциплины теперь
    private int enrollYear;
    private String programCode;
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private RPD body;

    @OneToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    @JsonManagedReference
    private DisciplineEntity discipline;

    public RPDEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPosition() {
        return authorPosition;
    }

    public void setAuthorPosition(String authorPosition) {
        this.authorPosition = authorPosition;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public RPD getBody() {
        return body;
    }

    public void setBody(RPD body) {
        this.body = body;
    }

    public int getEnrollYear() {
        return enrollYear;
    }

    public void setEnrollYear(int enrollYear) {
        this.enrollYear = enrollYear;
    }

    public DisciplineEntity getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineEntity discipline) {
        this.discipline = discipline;
    }
}
