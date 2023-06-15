package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailPaymentPlanDto {

    private Date paymentDate;

    private Integer installmentNumber;

    private Double principal;

    private Double interest;

    private Double fee;

    private Double charge;

    private Double total;

    private Double balance;
}
