package org.vaadin.example.backend.entity.savingBank;

import lombok.Data;

import java.util.Date;

@Data
public class DetailTransactionSanvingBank {

    private Integer catrnntra;

    private Date catrnftra;

    private Integer catrnndoc;

    private String catmvdesc;

    private Double catrnimpo;
}
