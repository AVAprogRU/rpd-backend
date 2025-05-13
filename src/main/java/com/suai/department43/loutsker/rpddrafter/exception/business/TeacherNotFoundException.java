package com.suai.department43.loutsker.rpddrafter.exception.business;

public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(long id) {
        super("Teacher with id: " + id + " not found");
    }
}
