package br.com.bcfinances.shared.presentation.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Captura exceções de ResponseEntities
@ControllerAdvice // Observa toda a aplicação
@RequiredArgsConstructor
public class BcFinancesExceptionHandler extends ResponseEntityExceptionHandler {

    @Qualifier("messageSource")
    private final MessageSource messageSorce;

    // Capturar mensagens que não conseguiram ler
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  org.springframework.http.HttpStatusCode status, 
                                                                  WebRequest request) {
        String msgUser = messageSorce.getMessage("msg.invalid", null, LocaleContextHolder.getLocale());
        String msgDev = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        List<Error> errors = Collections.singletonList(new Error(msgUser, msgDev));

        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, 
                                                                  org.springframework.http.HttpStatusCode status, 
                                                                  WebRequest request) {
        List<Error> errors = getErrorsList(ex.getBindingResult());
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }


    //	Tratando exceção do type abaixo
    @ExceptionHandler({ConstraintViolationException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest request) {
        String msgUser = messageSorce.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
        String msgDev = ex.toString();
        List<Error> errors = Collections.singletonList(new Error(msgUser, msgDev));

        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private List<Error> getErrorsList(BindingResult bindingResult) {
        List<Error> errors = new ArrayList<>();

//		Retornar todos os errors nos campos do objeto
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msgUser = messageSorce.getMessage(fieldError, LocaleContextHolder.getLocale());
            String msgDev = fieldError.toString();
            errors.add(new Error(msgUser, msgDev));
        }

        return errors;
    }

    @Data
    public static class Error {
        private String msgUser;
        private String msgDev;

        public Error(String msgUser, String msgDev) {
            super();
            this.msgUser = msgUser;
            this.msgDev = msgDev;
        }
    }

}
