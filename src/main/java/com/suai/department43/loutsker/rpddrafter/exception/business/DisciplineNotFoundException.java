package com.suai.department43.loutsker.rpddrafter.exception.business;

public class DisciplineNotFoundException extends RuntimeException {
    public DisciplineNotFoundException(long id) {
        super("DisciplineEntity with id: " + id + " not found");
    }
}
