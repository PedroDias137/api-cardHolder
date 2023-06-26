package com.example.apiportador.applicationservice.domain.entity;

import java.util.UUID;
import lombok.Builder;

public record CardHolder(
        UUID clientId,
        UUID creditAnalysisId,
        BankAccount bankAccount

) {
    @Builder(toBuilder = true)
    public CardHolder(UUID clientId, UUID creditAnalysisId, BankAccount bankAccount) {
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.bankAccount = bankAccount;
    }
}

