package com.bitreight.tasklist.dao.exception;

public class DaoSaveDuplicatedProjectException extends DaoException {

    public DaoSaveDuplicatedProjectException(String message) {
        super(message);
    }

    public DaoSaveDuplicatedProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
