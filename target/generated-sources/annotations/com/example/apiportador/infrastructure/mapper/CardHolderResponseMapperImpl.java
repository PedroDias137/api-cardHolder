package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-30T14:03:36-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class CardHolderResponseMapperImpl implements CardHolderResponseMapper {

    @Override
    public CardHolderResponse from(CardHolderEntity cardHolderEntity) {
        if ( cardHolderEntity == null ) {
            return null;
        }

        UUID cardHolderId = null;
        String status = null;
        BigDecimal limit = null;
        LocalDateTime createdAt = null;

        cardHolderId = cardHolderEntity.getCardHolderId();
        if ( cardHolderEntity.getStatus() != null ) {
            status = cardHolderEntity.getStatus().name();
        }
        limit = cardHolderEntity.getLimit();
        createdAt = cardHolderEntity.getCreatedAt();

        CardHolderResponse cardHolderResponse = new CardHolderResponse( cardHolderId, status, limit, createdAt );

        return cardHolderResponse;
    }
}
