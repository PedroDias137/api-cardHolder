package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.applicationservice.domain.entity.BankAccount;
import com.example.apiportador.applicationservice.domain.entity.CardHolder;
import com.example.apiportador.infrastructure.repository.entity.BankAccountEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-03T10:19:55-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class CardHolderEntityMapperImpl implements CardHolderEntityMapper {

    @Override
    public CardHolderEntity from(CardHolder cardHolder) {
        if ( cardHolder == null ) {
            return null;
        }

        CardHolderEntity.CardHolderEntityBuilder cardHolderEntity = CardHolderEntity.builder();

        cardHolderEntity.creditAnalysisId( cardHolder.creditAnalysisId() );
        cardHolderEntity.clientId( cardHolder.clientId() );
        cardHolderEntity.bankAccount( from( cardHolder.bankAccount() ) );

        return cardHolderEntity.build();
    }

    @Override
    public BankAccountEntity from(BankAccount bankAccount) {
        if ( bankAccount == null ) {
            return null;
        }

        BankAccountEntity.BankAccountEntityBuilder bankAccountEntity = BankAccountEntity.builder();

        bankAccountEntity.account( bankAccount.account() );
        bankAccountEntity.agency( bankAccount.agency() );
        bankAccountEntity.bankCode( bankAccount.bankCode() );

        return bankAccountEntity.build();
    }
}
