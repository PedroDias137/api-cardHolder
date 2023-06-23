package com.example.apiportador.presentation.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)

public record CardHolderResponse(
        UUID cardHolderId,
        String status,
        BigDecimal limit,
        LocalDateTime createdAt
) {
}
