package com.example.apiportador.applicationservice.cardholderservice;

import com.example.apiportador.applicationservice.domain.entity.CardHolder;
import com.example.apiportador.infrastructure.apicreditanalysis.ApiCreditAnalysis;
import com.example.apiportador.infrastructure.apicreditanalysis.dto.Credit;
import com.example.apiportador.infrastructure.mapper.CardHolderEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderResponseMapper;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.request.CardHolderRequest;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import com.example.apiportador.presentation.exception.ApiDownException;
import com.example.apiportador.presentation.exception.CardHolderAlreadyExistsException;
import com.example.apiportador.presentation.exception.ClientIdNotCompatibleException;
import com.example.apiportador.presentation.exception.CreditNotApprovedException;
import com.example.apiportador.presentation.exception.CreditNotFoundException;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import com.example.apiportador.util.StatusEnum;
import feign.FeignException;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCardHolderService {

    final String uuidRegex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[1-5][a-fA-F0-9]{3}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    private final ApiCreditAnalysis apiCreditAnalysis;
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderEntityMapper cardHolderEntityMapper;
    private final CardHolderResponseMapper cardHolderResponseMapper;
    private final CardHolderRepository cardHolderRepository;


    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:UnnecessaryParentheses"})
    public CardHolderResponse create(CardHolderRequest cardHolderRequest) throws ApiDownException {
        final Pattern uuidPatten = Pattern.compile(uuidRegex);

        if (!uuidPatten.matcher(cardHolderRequest.clientId()).matches() || !uuidPatten.matcher(cardHolderRequest.creditAnalysisId()).matches()) {
            throw new UuidOutOfFormatException("Digite um UUID válido");
        }

        if (cardHolderRepository.existsByClientId(UUID.fromString(cardHolderRequest.clientId()))) {
            throw new CardHolderAlreadyExistsException("Card Holder já existe");
        }

        final Credit credit;
        try {
            credit = apiCreditAnalysis.getAnalysiId(UUID.fromString(cardHolderRequest.creditAnalysisId()));
        } catch (FeignException.InternalServerError e) {
            throw new ApiDownException("Api fora do ar");
        } catch (FeignException.NotFound e) {
            throw new CreditNotFoundException("Análise não encontrada");
        }

        if (!credit.approved()) {
            throw new CreditNotApprovedException("Crédito não aprovado");
        }

        CardHolder cardHolder = cardHolderMapper.from(cardHolderRequest);
        if (cardHolder.bankAccount().agency() == null && cardHolder.bankAccount().bankCode() == null && cardHolder.bankAccount().account() == null) {
            cardHolder = cardHolder.toBuilder()
                    .bankAccount(null)
                    .build();
        }

        if (!cardHolder.clientId().equals(credit.clientId())) {
            throw new ClientIdNotCompatibleException("O id do cliente não conincide com o id do cliente cadastrado na análise");
        }

        CardHolderEntity cardHolderEntity = cardHolderEntityMapper.from(cardHolder);

        cardHolderEntity = cardHolderEntity.toBuilder()
                .limit(credit.approvedLimit())
                .status(StatusEnum.ACTIVE)
                .build();

        final CardHolderEntity cardHolderEntitySaved = cardHolderRepository.save(cardHolderEntity);

        final CardHolderResponse cardHolderResponse = cardHolderResponseMapper.from(cardHolderEntitySaved);

        return cardHolderResponse;
    }


}
