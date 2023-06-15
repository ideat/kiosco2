package org.vaadin.example.backend.entity.dpf;

import lombok.Data;

import java.util.Date;

@Data
public class DetailTransactionDpf {

    private String pftdtndep;

    private Date pftdtftra;

    private Integer pftdtttrn;

    private Integer pftdtitem;

    private Integer pftddtpref;

    private Integer pftdtccon;

    private String pftdtdesc;

    private Double pftdtimpp;

    private String pfcondesc;
}
