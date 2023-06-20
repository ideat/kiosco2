package org.vaadin.example.backend.entity.loan;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DeferredPaymentPlan {

    private Date prdipfreg;

    private Date prdipfpag;

    private Integer prdipcarg;

    private Double prdipcuot;

    private String prdipmpag;


}
