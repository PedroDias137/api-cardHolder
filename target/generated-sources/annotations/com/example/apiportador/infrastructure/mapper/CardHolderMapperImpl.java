package com.example.apiportador.infrastructure.mapper;

import com.example.apiportador.applicationservice.domain.entity.BankAccount;
import com.example.apiportador.applicationservice.domain.entity.CardHolder;
import com.example.apiportador.presentation.controller.request.CardHolderRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-23T14:14:48-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class CardHolderMapperImpl implements CardHolderMapper {

    @Override
    public CardHolder from(CardHolderRequest cardHolderRequest) {
        if ( cardHolderRequest == null ) {
            return null;
        }

        CardHolder.CardHolderBuilder cardHolder = CardHolder.builder();

        if ( cardHolderRequest.clientId() != null ) {
            cardHolder.clientId( cardHolderRequest.clientId().toString() );
        }
        if ( cardHolderRequest.creditAnalysisId() != null ) {
            cardHolder.creditAnalysisId( cardHolderRequest.creditAnalysisId().toString() );
        }
        cardHolder.bankAccount( from( cardHolderRequest.bankAccount() ) );

        return cardHolder.build();
    }

    @Override
    public BankAccount from(CardHolderRequest.BankAccountRequest bankAccountRequest) {
        if ( bankAccountRequest == null ) {
            return null;
        }

        BankAccount.BankAccountBuilder bankAccount = BankAccount.builder();

        bankAccount.account( bankAccountRequest.account() );
        bankAccount.agency( bankAccountRequest.agency() );
        bankAccount.bankCode( bankAccountRequest.bankCode() );

        return bankAccount.build();
    }
}
