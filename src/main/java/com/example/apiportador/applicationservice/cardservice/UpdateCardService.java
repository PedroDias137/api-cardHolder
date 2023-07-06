package com.example.apiportador.applicationservice.cardservice;

import com.example.apiportador.applicationservice.domain.entity.Card;
import com.example.apiportador.infrastructure.mapper.CardEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardMapper;
import com.example.apiportador.infrastructure.mapper.CardResponseMapper;
import com.example.apiportador.infrastructure.mapper.CardUpdateResponseMapper;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.CardRepository;
import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.request.CardRequest;
import com.example.apiportador.presentation.controller.response.CardUpdateResponse;
import com.example.apiportador.presentation.exception.CardHolderNotFoundException;
import com.example.apiportador.presentation.exception.CardNotFoundException;
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
public class UpdateCardService {

    final String uuidRegex = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[1-5][a-fA-F0-9]{3}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    final Pattern uuidPatten = Pattern.compile(uuidRegex);

    private final CardMapper cardMapper;
    private final CardEntityMapper cardEntityMapper;
    private final CardResponseMapper cardResponseMapper;
    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;

    private final CardUpdateResponseMapper cardUpdateResponseMapper;

    public CardUpdateResponse updateCardLimit(String cardHolderId, String cardId, CardRequest cardRequest) {

        if (!uuidPatten.matcher(cardHolderId).matches() || !uuidPatten.matcher(cardId).matches()) {
            throw new UuidOutOfFormatException("Digite um UUID válido");
        }

        final Optional<CardHolderEntity> cardHolderOpt = cardHolderRepository.findById(UUID.fromString(cardHolderId));
        if (cardHolderOpt.isEmpty()) {
            throw new CardHolderNotFoundException("Card Holder não encontrado");
        }
        final CardHolderEntity cardHolderEntity = cardHolderOpt.get();

        final Optional<CardEntity> cardOpt = cardRepository.findById(UUID.fromString(cardId));
        if (cardOpt.isEmpty()) {
            throw new CardNotFoundException("Card não encontrado");
        }

        final CardEntity cardEntity = cardOpt.get();

        if (!cardHolderId.equals(String.valueOf(cardEntity.getCardHolderId().getCardHolderId()))) {
            throw new UuidOutOfFormatException("Os ids não coincidem");
        }

        final Card card = cardMapper.from(cardRequest);

        if (card.limit().equals(BigDecimal.valueOf(0))) {
            throw new IllegalArgumentException("Digite um limite maior que 0");
        }
        final BigDecimal available = cardHolderEntity.getAvailableLimit().add(cardEntity.getLimit());

        if (available.compareTo(card.limit()) < 0) {
            throw new InsufficientLimitException("Você tem apenas R$%.2f de limite".formatted(available));
        }


        cardHolderRepository.updateAvailableLimitByCardHolderId(available.subtract(card.limit()), cardHolderEntity.getCardHolderId());

        cardRepository.updateLimitByCardId(card.limit(), cardEntity.getCardId());

        final Optional<CardEntity> cardEntityOptional = cardRepository.findById(cardEntity.getCardId());

        return cardUpdateResponseMapper.from(cardEntityOptional.get());
    }
}

