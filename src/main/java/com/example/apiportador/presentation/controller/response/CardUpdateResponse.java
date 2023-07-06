package com.example.apiportador.presentation.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardUpdateResponse(
        String cardId,
        BigDecimal limit
) {
}
