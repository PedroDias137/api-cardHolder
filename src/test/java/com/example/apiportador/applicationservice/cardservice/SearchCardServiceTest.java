package com.example.apiportador.applicationservice.cardservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.apiportador.infrastructure.mapper.CardResponseMapper;
import com.example.apiportador.infrastructure.mapper.CardResponseMapperImpl;
import com.example.apiportador.infrastructure.repository.CardRepository;
import com.example.apiportador.infrastructure.repository.entity.BankAccountEntity;
import com.example.apiportador.infrastructure.repository.entity.CardEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.response.CardResponse;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import com.example.apiportador.presentation.exception.CardNotFoundException;
import com.example.apiportador.util.StatusEnum;
import java.math.BigDecimal;
import java.util.List;
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
class SearchCardServiceTest {


    @InjectMocks
    private SearchCardService searchCardService;

    @Mock
    private CardRepository cardRepository;

    @Spy
    private CardResponseMapper cardResponseMapper = new CardResponseMapperImpl();

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;


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
    @DisplayName("Quando o uuid que foi passado na url não respeita os padrões deve retornar a exceção: UuidOutOfFormatException")
    void SholdReturnUuidOutOfFormatException() {
        assertThrows(UuidOutOfFormatException.class, () -> searchCardService.findAll("23fgvybuhni4k3ygt-e43rws"));
    }

    @Test
    @DisplayName("Deve retornar todos os cards de um card holder")
    void SholdReturnAllCardsOfCardHolder() {
        List<CardEntity> cardEntities = List.of(cardEntityFactory(), cardEntityFactory(), cardEntityFactory());
        Mockito.when(cardRepository.findByCardHolderId_CardHolderId(uuidArgumentCaptor.capture())).thenReturn(cardEntities);

        List<CardResponse> cardResponses = searchCardService.findAll(String.valueOf(UUID.randomUUID()));

        assertNotNull(cardResponses);
        assertEquals(3, cardResponses.size());
    }

    @Test
    @DisplayName("Quando algum  dos uuid que foram passados na url não respeita os padrões deve retornar a exceção: UuidOutOfFormatException")
    void sholdReturnUuidOutOfFormatException() {
        assertThrows(UuidOutOfFormatException.class, () -> searchCardService.findById("23fgvybuhni4k3ygt-e43rws", String.valueOf(UUID.randomUUID())));
        assertThrows(UuidOutOfFormatException.class,
                () -> searchCardService.findById(String.valueOf(UUID.randomUUID()), "ASNBDISD&*SDH#23453jklsndf"));
    }

    @Test
    @DisplayName("Quando passado o id de um cartão que não existe deve retornar a exceção: CardNotFoundException")
    void SholdReturnCardNotFoundException() {
        Optional<CardEntity> cardEntity = Optional.empty();
        Mockito.when(cardRepository.findById(uuidArgumentCaptor.capture())).thenReturn(cardEntity);
        assertThrows(CardNotFoundException.class,
                () -> searchCardService.findById(String.valueOf(UUID.randomUUID()), String.valueOf(UUID.randomUUID())));
    }

    @Test
    @DisplayName("Quando passado o id de um cartão que não existe deve retornar a exceção: CardNotFoundException")
    void SholdReturnUuidOutOfFormatExceptionFromFindById() {
        Mockito.when(cardRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.of(cardEntityFactory()));
        Exception e = assertThrows(UuidOutOfFormatException.class,
                () -> searchCardService.findById(String.valueOf(UUID.randomUUID()), String.valueOf(UUID.randomUUID())));
        assertEquals("Os ids não coincidem", e.getMessage());
    }

    @Test
    @DisplayName("Quando todos os requisitos são respeitados, deve retornar uma CardResponse")
    void SholdReturnCardEntity() {
        Optional<CardEntity> cardEntityOpt = Optional.of(cardEntityFactory());
        CardEntity cardEntity = cardEntityOpt.get();
        Mockito.when(cardRepository.findById(uuidArgumentCaptor.capture())).thenReturn(cardEntityOpt);
        CardResponse cardResponse = searchCardService.findById(String.valueOf(cardEntity.getCardHolderId().getCardHolderId()), String.valueOf(cardEntity.getCardId()));
        assertNotNull(cardResponse);
        assertEquals(CardResponse.class, cardResponse.getClass());
    }

}