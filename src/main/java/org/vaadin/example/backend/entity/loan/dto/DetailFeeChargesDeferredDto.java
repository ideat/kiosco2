package org.vaadin.example.backend.entity.loan.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DetailFeeChargesDeferredDto {

    private Integer sequence;

    private Date deferredDate;

    private Date dueDate;

    private Double principal;

    private Double interest;

    private Double fee;

    private Double charges;

    private Double total;


}
