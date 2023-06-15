package org.vaadin.example.backend.entity.savingBank.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BalanceSavingBankDto {

    private String fullName;

    private Integer codeClient;

    private String addressHome;

    private String homePhone;

    private String workPhone;

    private String account;

    private Date initDate;

    private Date endDate;

    private String currency;

    private List<DetailTransactionSavingBankDto> detailTransactionList;
}
