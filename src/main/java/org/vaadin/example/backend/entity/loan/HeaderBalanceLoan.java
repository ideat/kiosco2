package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class HeaderBalanceLoan {

    private Integer prmprnpre;

    private Integer prmprcage;

    private String gbagenomb;

    private Integer prmprplzo;

    private Integer prmprcmon;

    private String prtcrdesc;

    private Double prmprmapt;

    private Double prmprmdes;

    private Date prmprfdes;

    private String prmprstat;

    private Integer prmpruplz;

    private Date prmprfvac;
}
