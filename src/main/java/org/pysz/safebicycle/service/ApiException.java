package org.pysz.safebicycle.service;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
