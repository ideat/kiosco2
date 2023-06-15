package org.vaadin.example.backend.entity.loan;

import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
public class LoanAccounts {

    private String codeClient;

    private Integer numberLoan;

    private Double balance;

    private String currency;

    private String nameProduct;

    private String state;

    private Double rate;

    private Date expiredDate;

    public LocalDate getExpiredDateConvert(){
        return expiredDate.toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDate();
    }
}
