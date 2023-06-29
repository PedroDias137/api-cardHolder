package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.applicationservice.domain.entity.Card;
import com.example.apiportador.presentation.controller.request.CardRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card from(CardRequest cardRequest);
}
