package com.example.apiportador.infrastructure.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "CARD")
public class CardEntity {

    @Id
    @Column(name = "id")
    private UUID cardId;
    private String cardNumber;
    private int cvv;
    @Column(name = "card_limit")
    private BigDecimal limit;
    private LocalDate dueDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_holder_id")
    private CardHolderEntity cardHolderId;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @Builder(toBuilder = true)
    public CardEntity(BigDecimal limit, LocalDate dueDate, CardHolderEntity cardHolderId) {
        this.cardId = UUID.randomUUID();
        this.cardNumber = generateCardNumber();
        this.cvv = ThreadLocalRandom.current().nextInt(100, 1000);
        this.limit = limit;
        this.dueDate = LocalDate.now().plusYears(5);
        this.cardHolderId = cardHolderId;
    }

    public CardEntity() {
    }

    private String generateCardNumber() {
        String cardNumber = "4";
        Integer sum = 0;
        for (int i = 0; i < 4; i++) {
            final String numberTemp;
            if (i == 0 || i == 3) {
                numberTemp = String.valueOf(ThreadLocalRandom.current().nextInt(100, 1000));
            } else {
                numberTemp = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
            }
            cardNumber += numberTemp;
            if (i != 3) {
                cardNumber += " ";
            }
        }
        final String cardNumberTemp = cardNumber.replace(" ", "");
        for (int i = 0; i < cardNumberTemp.length(); i++) {
            final Character caracter = cardNumberTemp.charAt(i);
            Integer number = Integer.valueOf(String.valueOf(caracter));
            if (number != 0) {
                if (i % 2 == 0) {
                    number *= 2;
                    if (number > 9) {
                        number -= 9;
                    }
                }
            }
            sum += number;
        }
        Integer digito = 0;
        for (Integer j = 0; sum % 10 != 0; j++) {
            sum++;
            digito = j + 1;
        }
        cardNumber += digito;
        return cardNumber;
    }

    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public CardHolderEntity getCardHolderId() {
        return cardHolderId;
    }

    public void setCardHolderId(CardHolderEntity cardHolderId) {
        this.cardHolderId = cardHolderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
