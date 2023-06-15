package org.vaadin.example.backend.entity.dpf.dto;

import lombok.Data;

@Data
public class ProductRateTermAmount {

    private String productName;

    private Integer currency;

    private Integer initTerm;

    private Integer endTerm;

    private Double rate;

    private Double initAmount;

    private Double endAmount;

}
