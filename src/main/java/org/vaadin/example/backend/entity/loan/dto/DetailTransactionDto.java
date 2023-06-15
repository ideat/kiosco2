package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailTransactionDto {

    private Date paymentDate;

    private Integer transactionNumber;

    private Integer daysPayment;

    private Double paidInCapital;

    private Double paidInInterest;

    private Double paidInInterestPenal;

    private Double paidInForm;

    private Double paidInSecure;

    private Double paidInJudic;

    private Double paidInTotal;

    private Double paidInOther;

    private Double balance;
}
