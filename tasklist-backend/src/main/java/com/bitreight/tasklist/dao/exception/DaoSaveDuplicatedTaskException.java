package com.bitreight.tasklist.dao.exception;

public class DaoSaveDuplicatedTaskException extends DaoException {

    public DaoSaveDuplicatedTaskException(String message) {
        super(message);
    }

    public DaoSaveDuplicatedTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
