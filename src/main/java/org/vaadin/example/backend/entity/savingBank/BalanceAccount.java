package org.vaadin.example.backend.entity.savingBank;

import lombok.Data;

@Data
public class BalanceAccount {
    private String account;

    private Double balance;
}
