package com.example.apiportador.infrastructure.repository.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccountEntity {

    @Id
    private UUID id;
    @Column(name = "bank_account")
    private String account;
    private String agency;
    @Column(name = "bank_code")
    private String bankCode;


    public BankAccountEntity() {
    }

    @Builder
    public BankAccountEntity(UUID id, String account, String agency, String bankCode) {
        this.id = UUID.randomUUID();
        this.account = account;
        this.agency = agency;
        this.bankCode = bankCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
