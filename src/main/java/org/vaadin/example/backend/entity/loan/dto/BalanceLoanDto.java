package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BalanceLoanDto {

    private Integer numberLoan;

    private String fullNameClient;

    private String term;

    private String currency;

    private String typeLoan;

    private Double amountApproved;

    private Double amountDisbursed;

    private Date disbursementDate;

    private String state;

    private List<DetailTransactionDto> detailTransactionDtoList;

    private Date toDifferDate;

    private Double toDifferInterest;

    private Double toDifferCapital;

    private Double toDifferTotal;
}
