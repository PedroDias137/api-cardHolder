package com.example.apiportador.infrastructure.repository.entity;

import com.example.apiportador.util.StatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "CARD_HOLDER")
public class CardHolderEntity {

    @Id
    @Column(name = "id")
    private UUID cardHolderId;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "limit_total")
    private BigDecimal limit;

    private BigDecimal availableLimit;

    private UUID creditAnalysisId;

    private UUID clientId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_id")
    private BankAccountEntity bankAccount;

    public CardHolderEntity() {
    }

    @Builder(toBuilder = true)
    public CardHolderEntity(StatusEnum status, BigDecimal limit, UUID creditAnalysisId, UUID clientId, LocalDateTime createdAt,
                            BankAccountEntity bankAccount) {
        this.cardHolderId = UUID.randomUUID();
        this.status = status;
        this.limit = limit;
        this.availableLimit = limit;
        this.creditAnalysisId = creditAnalysisId;
        this.clientId = clientId;
        this.createdAt = createdAt;
        this.bankAccount = bankAccount;
    }

    public UUID getCardHolderId() {
        return cardHolderId;
    }

    public void setCardHolderId(UUID cardHolderId) {
        this.cardHolderId = cardHolderId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(BigDecimal availableLimit) {
        this.availableLimit = availableLimit;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountEntity bankAccount) {
        this.bankAccount = bankAccount;
    }

    public UUID getCreditAnalysisId() {
        return creditAnalysisId;
    }

    public void setCreditAnalysisId(UUID creditAnalysisId) {
        this.creditAnalysisId = creditAnalysisId;
    }
}
