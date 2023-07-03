package com.example.apiportador.presentation.exception.excepionhandler;


public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
