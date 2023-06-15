package org.vaadin.example.backend.entity.dpf.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BalanceDpfDto {

    private String numberDpf;

    private Date expiredDate;

    private String term;

    private Date registerDate;

    private Integer codeClient;

    private String currency;

    private String currencyName;

    private String fullName;

    private Double amount;

    private String product;

    private String beforeNumberDpf;

    private Date originalRegisterDate;

    private Integer numberRenovations;

    private Date processDate;

    private String paymentPeriod;

    private List<DetailTransactionDpfDto> detailTransaction;
}
