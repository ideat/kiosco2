package org.vaadin.example.backend.entity.savingBank.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailTransactionSavingBankDto {

    private Date transactionDate;

    private String description;

    private Double debit;  //debe

    private Double credit; //haber

    private Double amountReserved; //monto reservado, pignorado

    private Double balance;

}
