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
import com.example.apiportador.infrastructure.mapper.CardUpdateResponseMapper;
import com.example.apiportador.infrastructure.mapper.CardUpdateResponseMapperImpl;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.CardRepository;
import com.example.apiportador.infrastructure.repository.entity.BankAccountEntity;
import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.request.CardRequest;
import com.example.apiportador.presentation.controller.response.CardUpdateResponse;
import com.example.apiportador.presentation.exception.CardHolderNotFoundException;
import com.example.apiportador.presentation.exception.CardNotFoundException;
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
class UpdateCardServiceTest {

    @InjectMocks
    private UpdateCardService updateCardService;

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
    @Spy
    private CardUpdateResponseMapper cardUpdateResponseMapper = new CardUpdateResponseMapperImpl();

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCapture;

    @Captor
    private ArgumentCaptor<CardEntity> cardEntityArgumentCaptor;

    public static CardRequest cardRequestFactory() {
        return CardRequest.builder()
                .limit(BigDecimal.valueOf(1000))
                .build();
    }

    public static Card CardFactory() {
        return Card.builder()
                .cardHolderId(UUID.randomUUID())
                .limit(BigDecimal.valueOf(5000))
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
    @DisplayName("Quando os valores passados respeitam todas as validações deve retornar um cardResponse")
    void SholdReturnCardResponse() {
        CardEntity cardEntity = cardEntityFactory();
        CardHolderEntity cardHolderEntity = cardHolderEntityFactory();
        cardHolderEntity.setCardHolderId(UUID.fromString("d91fc637-57e1-4024-a5c2-de4bf2e05e9e"));
        Mockito.when(cardRepository.findById(uuidArgumentCapture.capture()))
                .thenReturn(Optional.of(cardEntity.toBuilder().cardHolderId(cardHolderEntity).build()));
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(Optional.of(cardHolderEntity));
        CardUpdateResponse cardResponse =
                updateCardService.updateCardLimit("d91fc637-57e1-4024-a5c2-de4bf2e05e9e", String.valueOf(UUID.randomUUID()), cardRequestFactory());
        assertNotNull(cardResponse);
    }

    @Test
    @DisplayName("Quando algum uuid que foi passado na url não respeita os padrões, deve retornar a exceção: UuidOutOfFormatException")
    void SholdReturnUuidOutOfFormatException() {
        assertThrows(UuidOutOfFormatException.class,
                () -> updateCardService.updateCardLimit("JDHADE32JI423ASDPO-", "dfsd fhj 43523", cardRequestFactory()));
    }

    @Test
    @DisplayName("Quando card holder não existe deve retornar a exceçãao: CardHolderNotFoundException")
    void shouldReturnCardHolderNotFoundException() {
        assertThrows(CardHolderNotFoundException.class,
                () -> updateCardService.updateCardLimit(String.valueOf(UUID.randomUUID()), String.valueOf(UUID.randomUUID()), cardRequestFactory()));

    }

    @Test
    @DisplayName("Quando passado o id de um cartão que não existe deve retornar a exceção: CardNotFoundException")
    void SholdReturnCardNotFoundException() {
        Optional<CardEntity> cardEntity = Optional.empty();
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(Optional.of(cardHolderEntityFactory()));
        Mockito.when(cardRepository.findById(uuidArgumentCapture.capture())).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class,
                () -> updateCardService.updateCardLimit(String.valueOf(UUID.randomUUID()), String.valueOf(UUID.randomUUID()), cardRequestFactory()));
    }

    @Test
    @DisplayName("Quando retornado do cardHolder entity um id que não coincide com o id co cardHolder passado na url deve retornar a exceção: UuidOutOfFormatException")
    void sholdReturnUuidOutOfFormatException() {
        Optional<CardEntity> cardEntity = Optional.empty();
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(Optional.of(cardHolderEntityFactory()));
        Mockito.when(cardRepository.findById(uuidArgumentCapture.capture())).thenReturn(Optional.of(cardEntityFactory()));
        Exception e = assertThrows(UuidOutOfFormatException.class,
                () -> updateCardService.updateCardLimit(String.valueOf(UUID.randomUUID()), String.valueOf(UUID.randomUUID()), cardRequestFactory()));
        assertEquals("Os ids não coincidem", e.getMessage());
    }

    @Test
    @DisplayName("Quando o valor requisitado é maior doque o limite disponivel deve retornar a exceção: InsufficientLimitException")
    void SholdInsufficientLimitException() {
        CardEntity cardEntity = cardEntityFactory();
        CardHolderEntity cardHolderEntity = cardHolderEntityFactory();
        cardHolderEntity.setCardHolderId(UUID.fromString("d91fc637-57e1-4024-a5c2-de4bf2e05e9e"));
        Mockito.when(cardRepository.findById(uuidArgumentCapture.capture()))
                .thenReturn(Optional.of(cardEntity.toBuilder().cardHolderId(cardHolderEntity).build()));
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(Optional.of(cardHolderEntity));
        assertThrows(InsufficientLimitException.class,
                () -> updateCardService.updateCardLimit("d91fc637-57e1-4024-a5c2-de4bf2e05e9e", String.valueOf(UUID.randomUUID()),
                        cardRequestFactory().toBuilder()
                                .limit(BigDecimal.valueOf(12000))
                                .build()));
    }

    @Test
    @DisplayName("Quando o valor requisitado é menor ou igual a zero deve retornar a exceção: IllegalArgumentException")
    void SholdIllegalArgumentException() {
        CardEntity cardEntity = cardEntityFactory();
        CardHolderEntity cardHolderEntity = cardHolderEntityFactory();
        cardHolderEntity.setCardHolderId(UUID.fromString("d91fc637-57e1-4024-a5c2-de4bf2e05e9e"));
        Mockito.when(cardRepository.findById(uuidArgumentCapture.capture()))
                .thenReturn(Optional.of(cardEntity.toBuilder().cardHolderId(cardHolderEntity).build()));
        Mockito.when(cardHolderRepository.findById(uuidArgumentCapture.capture())).thenReturn(Optional.of(cardHolderEntity));
        assertThrows(IllegalArgumentException.class,
                () -> updateCardService.updateCardLimit("d91fc637-57e1-4024-a5c2-de4bf2e05e9e", String.valueOf(UUID.randomUUID()),
                        cardRequestFactory().toBuilder()
                                .limit(BigDecimal.valueOf(0))
                                .build()));
    }

}