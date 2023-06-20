package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailFeeChargesDeferredDto {

    private Integer sequence;

    private Date deferredDate;

    private Date dueDate;

    private Date processDate;

    private Double principal;

    private Double interest;

    private Double fee;

    private Double charges;

    private Double total;

    private Double principalPending;
    private Double interestPending;
    private Double feePending;
    private Double chargesPending;
    private Double totalPending;

}
