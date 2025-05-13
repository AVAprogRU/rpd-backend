package com.suai.department43.loutsker.rpddrafter.exception.business;

public class AlreadyAssignedException extends RuntimeException {
    public AlreadyAssignedException(String teacherName, String disciplineName) {
        super("Already assigned: \"" + teacherName + "\" is already assigned with \"" + disciplineName + "\" RPD");
    }
}
