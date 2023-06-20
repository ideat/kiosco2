package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.util.Date;

@Data
public class HeaderChargeDeferred {

    private Double balanceInterest;

    private Double interestDeferred;

    private Double totalInterest;

    private Integer numberFeesInterest;

    private Double amountFeeInterest;
/////
    private Double balanceDeferred;

    private Double chargeToDeferred;

    private Double totalChargeToDeferred;

    private Integer numberFeesCharges;

    private Double amountFeeCharges;

    private Date  endPeriodGrace;

    private Integer numberCurrentFee;
}
