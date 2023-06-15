package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class DetailChargesDeferred {

    private Integer prdipnpre;

    private Integer prdipcarg;

    private Date prdipfpag;

    private Double prdipcuot;

    private Date prdipfreg;

    private Integer prdippref;

    private Integer prdipccon;

    private Integer prdipmrcb;
}
