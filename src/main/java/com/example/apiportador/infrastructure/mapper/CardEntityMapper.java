package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.applicationservice.domain.entity.Card;
import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardEntityMapper {

    CardEntity from(Card card);

    CardHolderEntity map(UUID cardHolderId);
}
