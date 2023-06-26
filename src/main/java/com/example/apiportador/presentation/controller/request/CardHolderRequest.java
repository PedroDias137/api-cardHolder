package com.example.apiportador.presentation.controller.request;

import lombok.Builder;


public record CardHolderRequest(
        String clientId,
        String creditAnalysisId,
        BankAccountRequest bankAccount

) {

    @Builder(toBuilder = true)
    public CardHolderRequest {
    }

    public record BankAccountRequest(String account, String agency, String bankCode) {

        @Builder(toBuilder = true)
        public BankAccountRequest {
        }

    }


}
