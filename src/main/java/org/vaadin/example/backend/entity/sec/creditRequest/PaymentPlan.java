package org.vaadin.example.backend.entity.sec.creditRequest;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PaymentPlan {
    private UUID id;

    private Integer numberRequest;

    private Integer quotaNumber;

    private LocalDate paymentDate;

    private Double capital;

    private Double interest;

    private Double secureCharge;

    private Double otherCharge;

    private Double itf;

    private Double fee;

    private Double residue;

}
