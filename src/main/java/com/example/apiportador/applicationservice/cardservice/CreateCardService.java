package com.example.apiportador.applicationservice.cardservice;


import com.example.apiportador.applicationservice.domain.entity.Card;
import com.example.apiportador.infrastructure.mapper.CardEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardMapper;
import com.example.apiportador.infrastructure.mapper.CardResponseMapper;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.CardRepository;
import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.request.CardRequest;
import com.example.apiportador.presentation.controller.response.CardResponse;
import com.example.apiportador.presentation.exception.CardHolderNotFoundException;
import com.example.apiportador.presentation.exception.InsufficientLimitException;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCardService {

    final String uuidRegex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[1-5][a-fA-F0-9]{3}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    final Pattern uuidPatten = Pattern.compile(uuidRegex);

    private final CardMapper cardMapper;
    private final CardEntityMapper cardEntityMapper;
    private final CardResponseMapper cardResponseMapper;
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;


    public CardResponse create(String cardHolderId, CardRequest cardRequest) {

        if (!uuidPatten.matcher(cardHolderId).matches() || !uuidPatten.matcher(cardRequest.cardHolderId()).matches()) {
            throw new UuidOutOfFormatException("Digite um UUID válido");
        }
        final Card card = cardMapper.from(cardRequest);

        final Optional<CardHolderEntity> cardHolderOpt = cardHolderRepository.findById(UUID.fromString(cardHolderId));

        if (cardHolderOpt.isEmpty()) {
            throw new CardHolderNotFoundException("Card Holder não encontrado");
        }

        if (!card.cardHolderId().equals(UUID.fromString(cardHolderId))) {
            throw new UuidOutOfFormatException("Os ids não coincidem");
        }

        final CardHolderEntity cardHolderEntity = cardHolderOpt.get();

        final BigDecimal available = cardHolderEntity.getAvailableLimit();


        if (available.compareTo(card.limit()) < 0) {
            throw new InsufficientLimitException("Você tem R$%.2f de limite".formatted(available));
        }

        cardHolderEntity.setAvailableLimit(available.subtract(card.limit()));
        final CardEntity cardEntity = cardEntityMapper.from(card);
        //cardEntity = cardEntity.toBuilder().cardHolderId(cardHolderEntity).build();
        final CardEntity cardEntitySaved = cardRepository.save(cardEntity);
        return cardResponseMapper.from(cardEntitySaved);
    }
}
