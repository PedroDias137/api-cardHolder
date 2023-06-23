package com.example.apiportador.presentation.exception;

public class CreditNotFoundException extends RuntimeException {

    public CreditNotFoundException(String message) {
        super(message);
    }
}
