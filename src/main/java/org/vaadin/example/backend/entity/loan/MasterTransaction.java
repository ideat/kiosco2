package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class MasterTransaction {

    private Integer prhtrntra;

    private Integer prhtrnpre;

    private Date prhtrftra;

    private Integer prhtrttrn;

    private Double prhtrimpt;


}
