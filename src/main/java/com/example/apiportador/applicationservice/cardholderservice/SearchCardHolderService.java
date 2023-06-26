package com.example.apiportador.applicationservice.cardholderservice;

import com.example.apiportador.infrastructure.apicreditanalysis.ApiCreditAnalysis;
import com.example.apiportador.infrastructure.mapper.CardHolderEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderResponseMapper;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import com.example.apiportador.presentation.exception.CardHolderNotFoundException;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import com.example.apiportador.util.StatusEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchCardHolderService {
    final String uuidRegex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[1-5][a-fA-F0-9]{3}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    private final ApiCreditAnalysis apiCreditAnalysis;
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderEntityMapper cardHolderEntityMapper;
    private final CardHolderResponseMapper cardHolderResponseMapper;
    private final CardHolderRepository cardHolderRepository;

    public List<CardHolderResponse> find(String status) {
        List<CardHolderEntity> cardHolderEntities = new ArrayList<>();

        if (status != null) {
            cardHolderEntities = cardHolderRepository.findByStatus(StatusEnum.valueOf(String.format(status).toUpperCase()));
        } else {
            cardHolderEntities = cardHolderRepository.findAll();
        }

        final List<CardHolderResponse> cardHolderResponses = cardHolderEntities.stream()
                .map(cardHolderResponseMapper::from)
                .toList();

        return cardHolderResponses;
    }

    public CardHolderResponse findById(String id) {

        final Pattern uuidPatten = Pattern.compile(uuidRegex);

        if (!uuidPatten.matcher(id).matches()) {
            throw new UuidOutOfFormatException("Digite um UUID válido");
        }

        final Optional<CardHolderEntity> cardHolderEntity = cardHolderRepository.findById(UUID.fromString(id));

        if (cardHolderEntity.isEmpty()) {
            throw new CardHolderNotFoundException("CardHolder não encontrado");
        }

        return cardHolderResponseMapper.from(cardHolderEntity.get());

    }

}
