package org.pysz.safebicycle.controller;

import org.pysz.safebicycle.rules.RuleEngineException;
import org.pysz.safebicycle.service.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({ApiException.class}) // according to documentation from swagger 400 will be thrown
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(ApiException ex, WebRequest req) {
        return ex.getMessage();
    }

    @ExceptionHandler({RuleEngineException.class}) // according to documentation from swagger 500 will be thrown
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(RuleEngineException ex, WebRequest req) {
        return ex.getMessage();
    }

}