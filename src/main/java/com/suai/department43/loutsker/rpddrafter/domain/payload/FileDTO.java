package com.suai.department43.loutsker.rpddrafter.domain.payload;

public class FileDTO {
    private String binaryString;

    public FileDTO() {
    }

    public FileDTO(String binaryString) {
        this.binaryString = binaryString;
    }

    public String getBinaryString() {
        return binaryString;
    }

    public void setBinaryString(String binaryString) {
        this.binaryString = binaryString;
    }
}
