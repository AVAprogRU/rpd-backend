package com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.runtime.Discipline;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "discipline")
public class DisciplineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    @JsonIgnore
    private Discipline body;

    public DisciplineEntity() {
    }

    public DisciplineEntity(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Discipline getBody() {
        return body;
    }

    public void setBody(Discipline body) {
        this.body = body;
    }
}
