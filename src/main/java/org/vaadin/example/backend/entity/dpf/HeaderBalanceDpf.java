package org.vaadin.example.backend.entity.dpf;

import lombok.Data;

import java.util.Date;

@Data
public class HeaderBalanceDpf {

    private String pfmdpndep;

    private Date pfmdpfvto;

    private Integer pfmdpplzo;

    private Date pfmdpfreg;

    private Integer gbagecage;

    private Integer pfmdpcmon;

    private String gbagenomb;

    private Double pfmdpcapi;

    private String pftdpdesc;

    private String pfmdpcorr;

    private Date pfmdpfror;

    private Integer pfmdpcren;

    private String pfmdpstat;

    private Date pfmdpfpro;

    private Integer pfmdpppgi;

}
