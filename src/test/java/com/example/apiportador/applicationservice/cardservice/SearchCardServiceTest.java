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
import com.example.apiportador.util.StatusEnum;
import java.math.BigDecimal;
import java.util.List;
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
        Mockito.when(cardRepository.findByCardHolderId(uuidArgumentCaptor.capture())).thenReturn(cardEntities);

        List<CardResponse> cardResponses = searchCardService.findAll(String.valueOf(UUID.randomUUID()));

        assertNotNull(cardResponses);
        assertEquals(3, cardResponses.size());
    }

}