package br.com.bcfinances.domain.exceptions;

import java.io.Serial;

public class PersonNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7284170813187369835L;

    public PersonNotFoundException(Long id) {
        super("Person not found with id: " + id);
    }

    public PersonNotFoundException(String message) {
        super(message);
    }
}