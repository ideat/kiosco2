package org.vaadin.example.backend.entity.savingBank;

import lombok.Data;

import java.util.Date;

@Data
public class SavingBankBalance {

    private String account;

    private Date transactionDate;

    private String transactionTime;

    private Integer agency;

    private String description;

    private Double assets; //haber

    private Double debit; //debe

    private Double balance; //saldo

    private String fullName;

    private String idCard;
}
