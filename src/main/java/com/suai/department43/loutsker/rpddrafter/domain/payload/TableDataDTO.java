package com.suai.department43.loutsker.rpddrafter.domain.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TableDataDTO {
    private String name;
    private List<String> headers;
    private TableDTO body;

    public TableDataDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public TableDTO getBody() {
        return body;
    }

    public void setBody(TableDTO body) {
        this.body = body;
    }
}
