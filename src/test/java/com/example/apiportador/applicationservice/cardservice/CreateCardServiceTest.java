package com.example.apiportador.applicationservice.cardservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.apiportador.applicationservice.domain.entity.Card;
import com.example.apiportador.infrastructure.mapper.CardEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardEntityMapperImpl;
import com.example.apiportador.infrastructure.mapper.CardMapper;
import com.example.apiportador.infrastructure.mapper.CardMapperImpl;
import com.example.apiportador.infrastructure.mapper.CardResponseMapper;
import com.example.apiportador.infrastructure.mapper.CardResponseMapperImpl;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.CardRepository;
import com.example.apiportador.infrastructure.repository.entity.BankAccountEntity;
import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.request.CardRequest;
import com.example.apiportador.presentation.controller.response.CardResponse;
import com.example.apiportador.presentation.exception.CardHolderNotFoundException;
import com.example.apiportador.presentation.exception.InsufficientLimitException;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import com.example.apiportador.util.StatusEnum;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCardServiceTest {

    @InjectMocks
    private CreateCardService createCardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardHolderRepository cardHolderRepository;

    @Spy
    private CardMapper cardMapper = new CardMapperImpl();

    @Spy
    private CardEntityMapper cardEntityMapper = new CardEntityMapperImpl();

    @Spy
    private CardResponseMapper cardResponseMapper = new CardResponseMapperImpl();

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCapture;

    @Captor
    private ArgumentCaptor<CardEntity> cardEntityArgumentCaptor;

    public static CardRequest cardRequestFactory() {
        return CardRequest.builder()
                .cardHolderId(String.valueOf(UUID.randomUUID()))
                .limit(BigDecimal.valueOf(1000))
                .build();
    }

    public static Card CardFactory() {
        return Card.builder()
                .cardHolderId(UUID.randomUUID())
                .limit(BigDecimal.valueOf(1000))
                .build();
    }

    public static BankAccountEntity bankAccountEntityFactory() {
        return BankAccountEntity.builder()
                .account("12345678-9")
                .agency("1234")
                .bankCode("123")
                .build();
    }

    public static CardHolderEntity cardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .clientId(UUID.fromString("d91fc637-57e1-4024-a5c2-de4bf2e05e9e"))
                .limit(BigDecimal.valueOf(10000.0))
                .status(StatusEnum.ACTIVE)
                .bankAccount(bankAccountEntityFactory())
                .build();
    }

    public static CardEntity cardEntityFactory() {
        return CardEntity.builder()
                .cardHolderId(cardHolderEntityFactory())
                .limit(BigDecimal.valueOf(1000))
                .build();

    }


    @Test
    @DisplayName("Quando algum uuid que foi passado na cardHolderRequest não respeita os padrões, deve retornar a exceção: UuidOutOfFormatException")
    void SholdReturnUuidOutOfFormatException() {
        assertThrows(UuidOutOfFormatException.class, () -> createCardService.create("JDHADE32JI423ASDPO-", cardRequestFactory()));
        assertThrows(UuidOutOfFormatException.class, () -> createCardService.create(String.valueOf(UUID.randomUUID()),
                cardRequestFactory().toBuilder().cardHolderId("rtfyughijo9k3p242").build()));
    }

    @Test
    @DisplayName("Quando card holder não existe deve retornar a exceçãao: CardHolderNotFoundException")
    void shouldReturnCardHolderNotFoundException() {
        assertThrows(CardHolderNotFoundException.class, () -> createCardService.create(String.valueOf(UUID.randomUUID()), cardRequestFactory()));
    }

    @Test
    @DisplayName("Quando O id do cardHolder passado na url é diferente do id do cardHolder passado na request deve retornar a exceção: UuidOutOfFormatException")
    void shouldUuidOutOfFormatException() {
        Optional<CardHolderEntity> cardHolderEntityOpt = Optional.of(cardHolderEntityFactory());
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(cardHolderEntityOpt);
        Exception e =
                assertThrows(UuidOutOfFormatException.class, () -> createCardService.create(String.valueOf(UUID.randomUUID()), cardRequestFactory()));
        assertEquals("Os ids não coincidem", e.getMessage());
    }


    @Test
    @DisplayName("Quando card holder tem limite mas é menor do que o limite requisitado deve retornar a exceçãao: InsufficientLimitException")
    void ShouldUuidInsufficientLimitException() {
        Optional<CardHolderEntity> cardHolderEntityOpt = Optional.of(cardHolderEntityFactory());
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(cardHolderEntityOpt);
        Exception e = assertThrows(InsufficientLimitException.class,
                () -> createCardService.create(String.valueOf(cardHolderEntityOpt.get().getCardHolderId()), cardRequestFactory().toBuilder()
                        .cardHolderId(String.valueOf(cardHolderEntityOpt.get().getCardHolderId()))
                        .limit(BigDecimal.valueOf(11000))
                        .build()));
        assertEquals("Você tem R$10000,00 de limite", e.getMessage());
    }

    @Test
    @DisplayName("Quando todas as validações são reseitadas deve retornar criar o cartão e retornar: CardResponse")
    void shouldReturnCardResponse() {
        Optional<CardHolderEntity> cardHolderEntityOpt = Optional.of(cardHolderEntityFactory());
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(cardHolderEntityOpt);
        Mockito.when(cardRepository.save(cardEntityArgumentCaptor.capture())).thenReturn(cardEntityFactory());
        CardResponse cardResponse = createCardService.create("d91fc637-57e1-4024-a5c2-de4bf2e05e9e", cardRequestFactory().toBuilder()
                        .cardHolderId("d91fc637-57e1-4024-a5c2-de4bf2e05e9e")
                        .build());
        assertNotNull(cardResponse);
    }

}