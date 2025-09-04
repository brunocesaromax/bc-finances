package br.com.bcfinances.person.domain.exceptions;

import java.io.Serial;

public class PersonExistentInTransactionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5901531250707613500L;

    public PersonExistentInTransactionException() {
        super("Person cannot be deleted because it has associated transactions");
    }
}