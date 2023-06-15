package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class HeaderPaymentPlan {

    private Integer prmprnpre;

    private Double prmprmapt;

    private Double prmprsald;

    private Date prmprfdes;

    private String prmprstat;

    private Integer prmprcage;

    private String gbagenomb;

    private String gbagendid;

    private String prtcrdesc;

    private Integer prmprppgi;

    private Integer prmprppgk;

    private String prcondesc;

    private Integer prmprcmon;

    private Integer prmprplzo;

    private Integer prmpruplz;
}
