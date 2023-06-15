package org.vaadin.example.backend.entity.dpf;

import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
public class DpfAccounts {
    private String numberDpf;

    private String numberCertificate;

    private Date expireDate;

    private Date renovationDate;

    private Integer term;

    private Date registerDate;

    private Integer codeClient;

    private String currency;

    private String fullName;

    private Double amount;

    private Double rate;

    private String product;

    public LocalDate getExpiredDateConvert(){
        return expireDate.toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDate();
    }

}
