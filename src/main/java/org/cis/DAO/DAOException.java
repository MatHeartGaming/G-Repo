package org.cis.DAO;

public class DAOException extends Exception{

    public DAOException() {}

    public DAOException(Exception exception) {
        super(exception);
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
