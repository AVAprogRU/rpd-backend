package com.suai.department43.loutsker.rpddrafter.exception.business;

public class FileDataExtractionException extends RuntimeException {
    public FileDataExtractionException(String extension) {
        super("Failed to extract data from " + extension + " file: unsupported data structure");
    }
}
