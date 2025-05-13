package com.suai.department43.loutsker.rpddrafter.domain.payload;

public class CheckSumDTO {
    private int row;
    private String text;
    private int[] columns;

    public CheckSumDTO() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int[] getColumns() {
        return columns;
    }

    public void setColumns(int[] columns) {
        this.columns = columns;
    }
}
