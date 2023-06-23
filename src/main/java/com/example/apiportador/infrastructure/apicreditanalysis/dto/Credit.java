package com.example.apiportador.infrastructure.apicreditanalysis.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Credit(

        BigDecimal approvedLimit,

        UUID clientId,

        boolean approved,

        LocalDateTime createdAt

) {
}
