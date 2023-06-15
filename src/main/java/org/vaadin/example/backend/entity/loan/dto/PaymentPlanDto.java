package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PaymentPlanDto {

    private Integer loanNumber;

    private Integer codeClient;

    private String fullName;

    private Double amount;

    private String currency;

    private String currencyName;

    private String typeLoan;

    private String state;

    private Double rate;

    private Integer term;

    private Date disbursementDate;

    private Integer paymentPeriodInterest;

    private Integer paymentPeriodPrincipal;

    private String feeType;

    private List<DetailPaymentPlanDto> detailPaymentPlanDtoList;

    private List<DeferredPaymentPlanDto> deferredPaymentPlanDtoList;
}
