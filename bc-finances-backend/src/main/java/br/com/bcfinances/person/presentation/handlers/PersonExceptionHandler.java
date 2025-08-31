package br.com.bcfinances.person.presentation.handlers;

import br.com.bcfinances.person.domain.exceptions.PersonExistentInTransactionException;
import br.com.bcfinances.person.domain.exceptions.PersonNotFoundException;
import br.com.bcfinances.presentation.exception.BcFinancesExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class PersonExceptionHandler {

    private final MessageSource messageSource;

    public PersonExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({PersonExistentInTransactionException.class})
    public ResponseEntity<Object> handlePersonExistentInTransactionException(PersonExistentInTransactionException ex) {
        String msgUser = messageSource.getMessage("person.existent.in.transaction", null, LocaleContextHolder.getLocale());
        String msgDev = Optional.ofNullable(ex.getCause()).isPresent() ? ex.getCause().toString() : ex.toString();
        List<BcFinancesExceptionHandler.Error> errors = Collections.singletonList(
            new BcFinancesExceptionHandler.Error(msgUser, msgDev)
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({PersonNotFoundException.class})
    public ResponseEntity<Object> handlePersonNotFoundException(PersonNotFoundException ex) {
        String msgUser = "Person not found";
        String msgDev = ex.getMessage();
        List<BcFinancesExceptionHandler.Error> errors = Collections.singletonList(
            new BcFinancesExceptionHandler.Error(msgUser, msgDev)
        );

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
}