package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.applicationservice.domain.entity.BankAccount;
import com.example.apiportador.applicationservice.domain.entity.CardHolder;
import com.example.apiportador.presentation.controller.request.CardHolderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderMapper {

    CardHolder from(CardHolderRequest cardHolderRequest);

    BankAccount from(CardHolderRequest.BankAccountRequest bankAccountRequest);
}
