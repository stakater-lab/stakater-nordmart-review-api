package com.stakater.nordmart.exception;

public class InvalidDataException extends Exception {
    public InvalidDataException(final String errorMessage) {
        super(errorMessage);
    }
}
