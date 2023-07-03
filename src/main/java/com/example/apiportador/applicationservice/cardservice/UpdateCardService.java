package com.example.apiportador.applicationservice.cardservice;

import com.example.apiportador.infrastructure.mapper.CardEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardMapper;
import com.example.apiportador.infrastructure.mapper.CardResponseMapper;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.CardRepository;
import com.example.apiportador.presentation.controller.request.CardRequest;
import com.example.apiportador.presentation.controller.response.CardResponse;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCardService {

    final String uuidRegex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[1-5][a-fA-F0-9]{3}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    final Pattern uuidPatten = Pattern.compile(uuidRegex);

    private final CardMapper cardMapper;
    private final CardEntityMapper cardEntityMapper;
    private final CardResponseMapper cardResponseMapper;
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;

    public CardResponse updateCardLimit(String cardHolderId, String cardId, CardRequest cardRequest){

        if (!uuidPatten.matcher(cardHolderId).matches() || !uuidPatten.matcher(cardId).matches()) {
            throw new UuidOutOfFormatException("Digite um UUID válido");
        }

        return null;
    }

}
