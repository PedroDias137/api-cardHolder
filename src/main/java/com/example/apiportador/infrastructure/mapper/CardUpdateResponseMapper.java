package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.presentation.controller.response.CardUpdateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardUpdateResponseMapper {
    CardUpdateResponse from(CardEntity cardEntity);
}
