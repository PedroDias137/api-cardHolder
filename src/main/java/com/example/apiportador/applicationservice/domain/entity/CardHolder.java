package com.example.apiportador.applicationservice.domain.entity;

import lombok.Builder;

public record CardHolder(
        String clientId,
        String creditAnalysisId,
        BankAccount bankAccount

) {
    @Builder(toBuilder = true)
    public CardHolder(String clientId, String creditAnalysisId, BankAccount bankAccount) {
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.bankAccount = bankAccount;
    }
}

