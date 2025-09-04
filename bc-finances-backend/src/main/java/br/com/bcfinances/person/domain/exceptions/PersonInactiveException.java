package br.com.bcfinances.person.domain.exceptions;

public class PersonInactiveException extends RuntimeException {

    public PersonInactiveException(String message) {
        super(message);
    }
}