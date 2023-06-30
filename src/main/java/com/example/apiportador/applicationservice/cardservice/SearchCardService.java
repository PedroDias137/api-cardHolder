package com.example.apiportador.applicationservice.cardservice;


import com.example.apiportador.infrastructure.mapper.CardEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardMapper;
import com.example.apiportador.infrastructure.mapper.CardResponseMapper;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.CardRepository;
import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.presentation.controller.response.CardResponse;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchCardService {

    final String uuidRegex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[1-5][a-fA-F0-9]{3}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    private final CardMapper cardMapper;
    private final CardEntityMapper cardEntityMapper;
    private final CardResponseMapper cardResponseMapper;
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;

    public List<CardResponse> findAll(String cardHolderId) {

        final Pattern uuidPatten = Pattern.compile(uuidRegex);

        if (!uuidPatten.matcher(cardHolderId).matches()) {
            throw new UuidOutOfFormatException("Digite um UUID válido");
        }

        final List<CardEntity> cardEntities = cardRepository.findByCardHolderId(UUID.fromString(cardHolderId));

        final List<CardResponse> cardResponses = cardEntities.stream().map(cardResponseMapper::from).toList();


        return cardResponses;
    }


}
