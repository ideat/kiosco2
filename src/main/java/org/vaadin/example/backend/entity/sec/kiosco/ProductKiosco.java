package org.vaadin.example.backend.entity.sec.kiosco;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductKiosco {
    private UUID id;

    private String concept;

    private String product;

    private Double minAmount;

    private Double maxAmount;

    private Double secure;

    private Double allRisk;

    private Integer term;

    private Double rate;

    private String guarantee;

    private String conditions;

    private String state;

}
