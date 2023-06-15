package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionNetbank {

    private Date prhtrftra;

    private Integer prtdtpref;

    private Integer prtdtccon;

    private Double prtdtimpp;

    private String prtdtdesc;
}
