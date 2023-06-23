package com.example.apiportador.presentation.exception;

public class CreditNotApprovedException extends RuntimeException {
    public CreditNotApprovedException(String message) {
        super(message);
    }
}
