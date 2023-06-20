package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailChargesDiferredPaymentPlanDto {

    private Integer sequence;

    private Date deferredDate;

    private Date expireDate;

    private Double principal;

    private Double interest;

    private Double interestDeferred;

    private Double fee;

    private Double charges;

    private Double chargesDeferred;

    private Double total;

    private Double balance;

}
