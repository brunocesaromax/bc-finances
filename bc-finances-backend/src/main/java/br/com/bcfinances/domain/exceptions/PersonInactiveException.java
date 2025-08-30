package br.com.bcfinances.domain.exceptions;

public class PersonInactiveException extends RuntimeException {

    public PersonInactiveException(String message) {
        super(message);
    }

    public PersonInactiveException(String message, Throwable cause) {
        super(message, cause);
    }
}