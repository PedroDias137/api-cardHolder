package com.example.apiportador.presentation.controller.request;

import java.math.BigDecimal;
import lombok.Builder;

public record CardRequest(

        String cardHolderId,
        BigDecimal limit

) {
    @Builder(toBuilder = true)
    public CardRequest {
    }
}
