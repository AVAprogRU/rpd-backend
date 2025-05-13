package com.suai.department43.loutsker.rpddrafter.domain.payload;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.enumeration.ErrorPointType;

public class TableCellError {
    private int row;
    private int column;
    private ErrorPointType type;

    public TableCellError(int row, int column, ErrorPointType type) {
        this.row = row;
        this.column = column;
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public ErrorPointType getType() {
        return type;
    }

    public void setType(ErrorPointType type) {
        this.type = type;
    }
}
