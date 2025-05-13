package com.suai.department43.loutsker.rpddrafter.domain.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherInputDTO {
    private HashMap<String, String> placeholders;
    private List<TableDataDTO> tables;

    public TeacherInputDTO() {
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
}
