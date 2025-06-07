package com.github.losevskiyfz.exception;

public class BadUuidRequestException extends RuntimeException{
    public BadUuidRequestException(String message) {
        super(message);
    }
}
