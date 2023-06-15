package org.vaadin.example.backend.entity.dpf.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailTransactionDpfDto {

    private Date transactionDate;

    private String typeTransaction;

    private String description;

    private Double amount;

}
