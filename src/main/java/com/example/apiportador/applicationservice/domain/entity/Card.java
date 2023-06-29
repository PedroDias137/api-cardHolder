package com.example.apiportador.applicationservice.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record Card(
        UUID cardHolderId,
        BigDecimal limit

) {

    @Builder(toBuilder = true)
    public Card(UUID cardHolderId, BigDecimal limit) {
        this.cardHolderId = cardHolderId;
        this.limit = limit;
    }
}
