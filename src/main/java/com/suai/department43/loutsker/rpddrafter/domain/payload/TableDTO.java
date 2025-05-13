package com.suai.department43.loutsker.rpddrafter.domain.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TableDTO {
    private boolean columnNumbering;
    private boolean rowNumbering;
    private List<List<String>> rows;
    private CheckSumDTO totalSum;
    private CheckSumDTO[] checkSums;
    private InRowHeaderDTO[] inRowHeaders;
    private ImageDTO[] images;

    public TableDTO() {
    }

    public TableDTO(List<List<String>> rows) {
        this.rows = rows;
    }

    public ImageDTO[] getImages() {
        return images;
    }

    public void setImages(ImageDTO[] images) {
        this.images = images;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    public CheckSumDTO getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(CheckSumDTO totalSum) {
        this.totalSum = totalSum;
    }

    public boolean isColumnNumbering() {
        return columnNumbering;
    }

    public void setColumnNumbering(boolean columnNumbering) {
        this.columnNumbering = columnNumbering;
    }

    public boolean isRowNumbering() {
        return rowNumbering;
    }

    public void setRowNumbering(boolean rowNumbering) {
        this.rowNumbering = rowNumbering;
    }

    public CheckSumDTO[] getCheckSums() {
        return checkSums;
    }

    public void setCheckSums(CheckSumDTO[] checkSums) {
        this.checkSums = checkSums;
    }

    public InRowHeaderDTO[] getInRowHeaders() {
        return inRowHeaders;
    }

    public void setInRowHeaders(InRowHeaderDTO[] inRowHeaders) {
        this.inRowHeaders = inRowHeaders;
    }
}
