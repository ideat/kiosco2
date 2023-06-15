package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DeferredPaymentPlanDto {

    private Integer secuence;

    private Date deferredDate;

    private Date expireDate;

    private Double principal;

    private Double interest;

    private Double charge;

    private Double fee;

    private Double total;

    private String isPayment;
}
