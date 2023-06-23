package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.applicationservice.domain.entity.BankAccount;
import com.example.apiportador.applicationservice.domain.entity.CardHolder;
import com.example.apiportador.infrastructure.repository.entity.BankAccountEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderEntityMapper {

    CardHolderEntity from(CardHolder cardHolder);

    BankAccountEntity from(BankAccount bankAccount);
}
