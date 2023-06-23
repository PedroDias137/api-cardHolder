package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderResponseMapper {

    CardHolderResponse from(CardHolderEntity cardHolderEntity);


}
