package org.vaadin.example.backend.entity.sec;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SummaryCreditRequestStage {

    private Integer numberRequest;

    private Double amount;

    private String currency;

    private LocalDate requestDate;

    private String state;

    private String stage;

    private Double rateInterest;

    private Integer term;

    private String typeTerm;

    private Integer paymentPeriod;

    private String typeFee;

    private String officerName;

    private String assignedUser;
}
