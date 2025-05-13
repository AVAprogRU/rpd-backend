package com.suai.department43.loutsker.rpddrafter.exception.business;

public class FileRecognitionException extends RuntimeException {
    public FileRecognitionException(String targetExtension) {
        super("Unable to recognize file as " + targetExtension + " file");
    }
}
