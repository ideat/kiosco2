package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailDeferredPaymentPlanDto {

    private Integer secuence;

    private Date deferredDate;

    private Double principal;

    private Double interest;

    private Double interestDeferred;

    private Double fee;

    private Double charges;

    private Double chargesDiferred;

    private Double total;
}
