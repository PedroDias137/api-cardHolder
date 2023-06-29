package com.example.apiportador.applicationservice.cardholderservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.apiportador.applicationservice.domain.entity.BankAccount;
import com.example.apiportador.applicationservice.domain.entity.CardHolder;
import com.example.apiportador.infrastructure.apicreditanalysis.ApiCreditAnalysis;
import com.example.apiportador.infrastructure.apicreditanalysis.dto.Credit;
import com.example.apiportador.infrastructure.mapper.CardHolderEntityMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderEntityMapperImpl;
import com.example.apiportador.infrastructure.mapper.CardHolderMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderMapperImpl;
import com.example.apiportador.infrastructure.mapper.CardHolderResponseMapper;
import com.example.apiportador.infrastructure.mapper.CardHolderResponseMapperImpl;
import com.example.apiportador.infrastructure.repository.CardHolderRepository;
import com.example.apiportador.infrastructure.repository.entity.BankAccountEntity;
import com.example.apiportador.infrastructure.repository.entity.CardHolderEntity;
import com.example.apiportador.presentation.controller.request.CardHolderRequest;
import com.example.apiportador.presentation.controller.response.CardHolderResponse;
import com.example.apiportador.presentation.exception.ApiDownException;
import com.example.apiportador.presentation.exception.CardHolderAlreadyExistsException;
import com.example.apiportador.presentation.exception.ClientIdNotCompatibleException;
import com.example.apiportador.presentation.exception.CreditNotApprovedException;
import com.example.apiportador.presentation.exception.CreditNotFoundException;
import com.example.apiportador.presentation.exception.UuidOutOfFormatException;
import com.example.apiportador.util.StatusEnum;
import feign.FeignException;
import feign.RetryableException;
import java.math.BigDecimal;
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
class CreateCardHolderServiceTest {

    @Mock
    private CardHolderRepository cardHolderRepository;

    @Mock
    private ApiCreditAnalysis apiCreditAnalysis;

    @Spy
    private CardHolderEntityMapper cardHolderEntityMapper = new CardHolderEntityMapperImpl();

    @Spy
    private CardHolderMapper cardHolderMapper = new CardHolderMapperImpl();

    @Spy
    private CardHolderResponseMapper cardHolderResponseMapper = new CardHolderResponseMapperImpl();

    @InjectMocks
    private CreateCardHolderService cardHolderService;

    @Captor
    private ArgumentCaptor<String> idArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    private ArgumentCaptor<CardHolderEntity> cardHolderArgumentCaptor;

    public static BankAccount bankAccountFactory() {
        return BankAccount.builder()
                .account("12345678-9")
                .agency("1234")
                .bankCode("123")
                .build();
    }

    public static CardHolder cardHolderFactory() {
        return CardHolder.builder()
                .clientId(UUID.fromString("a0847eeb-9bd7-4201-be29-9b2e3967a56b"))
                .creditAnalysisId(UUID.fromString("bc0e54f4-b4f9-43c9-b4d8-24e4ed545a47"))
                .bankAccount(bankAccountFactory())
                .build();
    }


    public static CardHolderRequest.BankAccountRequest bankAccountRequestFactory() {
        return CardHolderRequest.BankAccountRequest.builder()
                .account("12345678-9")
                .agency("1234")
                .bankCode("123")
                .build();
    }

    public static CardHolderRequest cardHolderRequestFactory() {
        return CardHolderRequest.builder()
                .clientId("a0847eeb-9bd7-4201-be29-9b2e3967a56b")
                .creditAnalysisId("bc0e54f4-b4f9-43c9-b4d8-24e4ed545a47")
                .bankAccount(bankAccountRequestFactory())
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
                .clientId(UUID.fromString("a0847eeb-9bd7-4201-be29-9b2e3967a56b"))
                .limit(BigDecimal.valueOf(10000.0))
                .status(StatusEnum.ACTIVE)
                .bankAccount(bankAccountEntityFactory())
                .build();
    }

    public static Credit creditFactory() {
        return Credit.builder()
                .approvedLimit(BigDecimal.valueOf(10000.0))
                .approved(true)
                .clientId(UUID.fromString("a0847eeb-9bd7-4201-be29-9b2e3967a56b"))
                .createdAt(null)
                .build();
    }

    @Test
    @DisplayName("Quando passado o id de um client que já existe deve retornar a exceção: CardHolderAlreadyExistsException ")
    void shoudReturnCardHolderAlreadyExistsException() {
        Mockito.when(cardHolderRepository.existsByClientId(uuidArgumentCaptor.capture())).thenReturn(true);
        assertThrows(CardHolderAlreadyExistsException.class, () -> cardHolderService.create(cardHolderRequestFactory()));
    }

    @Test
    @DisplayName("Quando api estiver fora do ar deve retornar o erro: ApiDownException")
    void shouldReturnApiDownException() {
        final CardHolderRequest cardHolderRequest = cardHolderRequestFactory();
        Mockito.when(apiCreditAnalysis.getAnalysiId(uuidArgumentCaptor.capture())).thenThrow(FeignException.InternalServerError.class);
        assertThrows(ApiDownException.class, () -> cardHolderService.create(cardHolderRequest));
    }

    @Test
    @DisplayName("Quando passado o id de uma análise inesistente deve retornar o erro: CreditNotFoundException")
    void shouldReturnCreditNotFoundException() {
        final CardHolderRequest cardHolderRequest = cardHolderRequestFactory();
        Mockito.when(apiCreditAnalysis.getAnalysiId(uuidArgumentCaptor.capture())).thenThrow(FeignException.NotFound.class);
        assertThrows(CreditNotFoundException.class, () -> cardHolderService.create(cardHolderRequest));
    }


    @Test
    @DisplayName("Quando crédito não esta aprovado deve retornar a exceção: CreditNotApprovedException")
    void SholdReturnCreditNotApprovedException() {
        final Credit credit = creditFactory().toBuilder().approved(false).build();
        Mockito.when(apiCreditAnalysis.getAnalysiId(uuidArgumentCaptor.capture())).thenReturn(credit);

        assertThrows(CreditNotApprovedException.class, () -> cardHolderService.create(cardHolderRequestFactory()));
    }

    @Test
    @DisplayName("Quando algum uuid que foi passado na cardHolderRequest não respeita os padrões, deve retornar a exceção: UuidOutOfFormatException")
    void SholdReturnUuidOutOfFormatException() {
        assertThrows(UuidOutOfFormatException.class, () -> cardHolderService.create(cardHolderRequestFactory().toBuilder().clientId("asdyuinjfksgldhfjk2135768").build()));
        assertThrows(UuidOutOfFormatException.class, () -> cardHolderService.create(cardHolderRequestFactory().toBuilder().creditAnalysisId("asdyuinjfksgldhfjk2135768").build()));
    }

    @Test
    @DisplayName("Quando passado uma conta bancária com valores nulos deve deixar o objeto inteiro de conta bancaria nulo")
    void ShouldReturnCardHolderWithBankAccountNull() throws ApiDownException {
        Mockito.when(apiCreditAnalysis.getAnalysiId(uuidArgumentCaptor.capture())).thenReturn(creditFactory());
        Mockito.when(cardHolderRepository.save(cardHolderArgumentCaptor.capture())).thenReturn(cardHolderEntityFactory());

        CardHolderRequest.BankAccountRequest bankAccountRequest = new CardHolderRequest.BankAccountRequest(null, null, null);

        cardHolderService.create(cardHolderRequestFactory().toBuilder().bankAccount(bankAccountRequest).build());

        assertNull(cardHolderArgumentCaptor.getValue().getBankAccount());
    }

    @Test
    @DisplayName("Quando apiAnalysis retorna uma analise com o id do cliente diferente do id do cliente atual deve retornar o erro: ClientIdNotCompatibleException")
    void ShouldReturnClientIdNotCompatibleException() {
        Mockito.when(apiCreditAnalysis.getAnalysiId(uuidArgumentCaptor.capture()))
                .thenReturn(creditFactory().toBuilder().clientId(UUID.randomUUID()).build());
        assertThrows(ClientIdNotCompatibleException.class, () -> cardHolderService.create(cardHolderRequestFactory()));
    }

    @Test
    @DisplayName("Quando passado todos os valores corretos deve retornar um CardHolderResponse")
    void ShouldReturnCardHolderResponse() throws ApiDownException {
        CardHolderEntity cardHolderEntity = cardHolderEntityFactory();
        Mockito.when(apiCreditAnalysis.getAnalysiId(uuidArgumentCaptor.capture())).thenReturn(creditFactory());
        Mockito.when(cardHolderRepository.existsByClientId(uuidArgumentCaptor.capture())).thenReturn(false);
        Mockito.when(cardHolderRepository.save(cardHolderArgumentCaptor.capture())).thenReturn(cardHolderEntity);

        CardHolderResponse cardHolderResponse = cardHolderService.create(cardHolderRequestFactory());

        assertNotNull(cardHolderResponse);
    }

}