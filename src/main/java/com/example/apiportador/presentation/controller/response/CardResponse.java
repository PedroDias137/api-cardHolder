package com.example.apiportador.presentation.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardResponse(
        UUID cardId,
        String cardNumber,
        int cvv,
        LocalDate dueDate
) {
}
