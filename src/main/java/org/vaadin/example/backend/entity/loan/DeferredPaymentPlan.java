package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class DeferredPaymentPlan {

    private Date prdipfreg;

    private Date prdipfpag;

    private Integer prdipcarg;

    private Double prdipcuot;

    private String prdipmpag;
}
