package br.com.bcfinances.shared.presentation.exception;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
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

@ControllerAdvice
@RequiredArgsConstructor
public class BcFinancesExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  org.springframework.http.HttpStatusCode status,
                                                                  WebRequest request) {
        String msgUser = messageSource.getMessage("msg.invalid", null, LocaleContextHolder.getLocale());
        String msgDev = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        List<Error> errors = buildErrorList(msgUser, msgDev);

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


    @ExceptionHandler({ConstraintViolationException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest request) {
        String msgUser = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
        String msgDev = ex.toString();
        List<Error> errors = buildErrorList(msgUser, msgDev);

        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultHandleException(Exception ex, WebRequest request) {
        String msgUser = messageSource.getMessage("internal.error", null, LocaleContextHolder.getLocale());
        String msgDev = ex.toString();
        List<Error> errors = buildErrorList(msgUser, msgDev);

        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private List<Error> getErrorsList(BindingResult bindingResult) {
        List<Error> errors = new ArrayList<>();

        // Return all errors in the object fields
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msgUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String msgDev = fieldError.toString();
            errors.add(buildError(msgUser, msgDev));
        }

        return errors;
    }

    public static List<Error> buildErrorList(String msgUser, String msgDev) {
        return Collections.singletonList(buildError(msgUser, msgDev));
    }

    private static Error buildError(String msgUser, String msgDev) {
        SpanContext context = Span.current().getSpanContext();

        if (context.isValid()) {
            return new Error(msgUser, msgDev, context.getTraceId(), context.getSpanId());
        }

        return new Error(msgUser, msgDev);
    }

    @AllArgsConstructor
    @Getter
    public static class Error {
        private String msgUser;
        private String msgDev;
        private String traceId;
        private String spanId;

        public Error(String msgUser, String msgDev) {
            this.msgUser = msgUser;
            this.msgDev = msgDev;
        }
    }

}
