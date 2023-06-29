package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.presentation.controller.response.CardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardResponseMapper {

    CardResponse from(CardEntity cardEntity);
}
