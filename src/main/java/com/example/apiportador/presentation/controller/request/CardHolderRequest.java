package com.example.apiportador.presentation.controller.request;

import java.util.UUID;
import lombok.Builder;


public record CardHolderRequest(
        UUID clientId,
        UUID creditAnalysisId,
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
