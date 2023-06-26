package com.example.apiportador.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.apiportador.infrastructure.mapper.CardHolderResponseMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderResponseMapperImpl;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.entity.BankAccountEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import com.example.apiportador.presentation.exception.CardHolderNotFoundException;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import com.example.apiportador.util.StatusEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
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
class SearchCardHolderServiceTest {


    @InjectMocks
    private SearchCardHolderService searchCardHolderService;
    @Mock
    private CardHolderRepository cardHolderRepository;
    @Spy
    private CardHolderResponseMapper cardHolderResponseMapper = new CardHolderResponseMapperImpl();
    @Captor
    private ArgumentCaptor<StatusEnum> statusArgumentCaptor;
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
                .clientId(UUID.fromString("a0847eeb-9bd7-4201-be29-9b2e3967a56b"))
                .limit(BigDecimal.valueOf(10000.0))
                .status(StatusEnum.ACTIVE)
                .bankAccount(bankAccountEntityFactory())
                .build();
    }

    @Test
    @DisplayName("Quando passado um status nulo,deve retornar todos os cardHolder")
    void SholdReturnAllCardHolder() {
        List<CardHolderEntity> cardHolderEntities = new ArrayList<>();
        String status = null;
        cardHolderEntities.add(cardHolderEntityFactory());
        cardHolderEntities.add(cardHolderEntityFactory());
        cardHolderEntities.add(cardHolderEntityFactory().toBuilder().status(StatusEnum.INACTIVE).build());
        cardHolderEntities.add(cardHolderEntityFactory().toBuilder().status(StatusEnum.INACTIVE).build());
        cardHolderEntities.add(cardHolderEntityFactory().toBuilder().status(StatusEnum.INACTIVE).build());
        cardHolderEntities.add(cardHolderEntityFactory());
        cardHolderEntities.add(cardHolderEntityFactory());

        Mockito.when(cardHolderRepository.findAll()).thenReturn(cardHolderEntities);

        searchCardHolderService.find(status);

        assertEquals(7, cardHolderEntities.size());
    }

    @Test
    @DisplayName("Quando passado o status inactive,deve retornar somente os cardHolders inativos")
    void SholdReturnInactiveCardHolder() {
        List<CardHolderEntity> cardHolderEntities = new ArrayList<>();
        String status = "inactive";
        cardHolderEntities.add(cardHolderEntityFactory().toBuilder().status(StatusEnum.INACTIVE).build());
        cardHolderEntities.add(cardHolderEntityFactory().toBuilder().status(StatusEnum.INACTIVE).build());
        cardHolderEntities.add(cardHolderEntityFactory().toBuilder().status(StatusEnum.INACTIVE).build());

        Mockito.when(cardHolderRepository.findByStatus(statusArgumentCaptor.capture())).thenReturn(cardHolderEntities);

        searchCardHolderService.find(status);

        final Integer[] inactive = {0};
        final Integer[] active = {0};
        cardHolderEntities.forEach(cardHolderEntity -> {
            if (cardHolderEntity.getStatus().equals(StatusEnum.valueOf(status.toUpperCase()))) {
                inactive[0]++;
            } else {
                active[0]++;
            }
        });
        assertEquals(cardHolderEntities.size(), inactive[0]);
        assertEquals(0, active[0]);

    }

    @Test
    @DisplayName("Quando passado um status active, deve retornar apenas os cardHlders ativos")
    void SholdReturnActiveCardHolder() {
        List<CardHolderEntity> cardHolderEntities = new ArrayList<>();
        String status = "active";
        cardHolderEntities.add(cardHolderEntityFactory());
        cardHolderEntities.add(cardHolderEntityFactory());
        cardHolderEntities.add(cardHolderEntityFactory());

        Mockito.when(cardHolderRepository.findByStatus(statusArgumentCaptor.capture())).thenReturn(cardHolderEntities);

        searchCardHolderService.find(status);

        final Integer[] inactive = {0};
        final Integer[] active = {0};
        cardHolderEntities.forEach(cardHolderEntity -> {
            if (cardHolderEntity.getStatus().equals(StatusEnum.valueOf(status.toUpperCase()))) {
                active[0]++;
            } else {
                inactive[0]++;
            }
        });
        assertEquals(cardHolderEntities.size(), active[0]);
        assertEquals(0, inactive[0]);
    }

    @Test
    @DisplayName("Quando o uuid passado não respeita os padrões, deve retornar a exceção: UuidOutOfFormatException")
    void SholdReturnUuidOutOfFormatException() {
        assertThrows(UuidOutOfFormatException.class, () -> searchCardHolderService.findById("sdfkdsfkjds324242"));
    }

    @Test
    @DisplayName("Quando passado o uuid de um card holder que não existe deve retornar a exceção: CardHolderNotFoundException")
    void SholdReturnCardHolderNotFoundException(){
        Optional<CardHolderEntity> cardHolderEntity = Optional.empty();
        Mockito.when(cardHolderRepository.findById(uuidArgumentCaptor.capture())).thenReturn(cardHolderEntity);

        assertThrows(CardHolderNotFoundException.class, () -> searchCardHolderService.findById(String.valueOf(UUID.randomUUID())));
    }

    @Test
    @DisplayName("Quando passado o uuid de um card holder deve retornar o mesmo")
    void SholdReturnCardHolder(){

        Mockito.when(cardHolderRepository.findById(uuidArgumentCaptor.capture())).thenReturn(Optional.ofNullable(cardHolderEntityFactory()));

        CardHolderResponse cardHolderResponse = searchCardHolderService.findById(String.valueOf(UUID.randomUUID()));

        assertNotNull(cardHolderResponse);

    }
}