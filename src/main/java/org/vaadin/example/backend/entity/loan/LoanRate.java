package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class LoanRate {

    private Integer prtsanpre;

    private Date prtsafvig;

    private Double prtsatbas;

}
