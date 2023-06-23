package com.example.apiportador.applicationservice.domain.entity;

import jakarta.validation.constraints.Size;
import java.util.regex.Pattern;
import lombok.Builder;

public record BankAccount(

        @Size(min = 10, max = 10, message = "Número da conta deve conter 10 dígitos")
        String account,
        @Size(min = 4, max = 4, message = "Agencia deve conter 4 dígitos")
        String agency,
        @Size(min = 3, max = 3, message = "Código do banco deve conter 3 dígitos")
        String bankCode
) {
    @Builder
    public BankAccount(String account, String agency, String bankCode) {

        if (bankCode != null && !Pattern.compile("\\d{3}").matcher(bankCode).matches()) {
            throw new IllegalArgumentException("Digite um código de banco válido");
        }

        if (agency != null && !Pattern.compile("\\d{4}").matcher(agency).matches()) {
            throw new IllegalArgumentException("Digite uma agência válida");
        }

        if (account != null && !Pattern.compile("^\\d{8}-\\d$|^\\d{10}$|^\\d{8}-[Xx]$").matcher(account).matches()) {
            throw new IllegalArgumentException("Digite uma agência válida");
        }

        this.bankCode = bankCode;
        this.account = account;
        this.agency = agency;
    }
}
