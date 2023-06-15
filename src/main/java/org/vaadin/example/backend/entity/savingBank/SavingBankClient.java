package org.vaadin.example.backend.entity.savingBank;

import lombok.Data;

@Data
public class SavingBankClient {
    private String fullName;

    private String account;

    private String currency;

    private String productName;

    private Double balance;

    private String state;

    private Double rate;
}
