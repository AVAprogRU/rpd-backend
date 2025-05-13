package com.suai.department43.loutsker.rpddrafter.domain.payload;

import java.io.Serializable;

public class FIODTO implements Serializable {
    private String name;
    private String lastname;
    private String patronymic;

    public FIODTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
}
