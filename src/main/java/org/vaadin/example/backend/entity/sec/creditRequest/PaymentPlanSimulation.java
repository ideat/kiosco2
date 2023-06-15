package org.vaadin.example.backend.entity.sec.creditRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentPlanSimulation {

    private Double amount;

    private Double rate;

    private String fullName;

    private Integer term;

    private String typeFee;

    private String productName;

    private String destiny;

    private Double secure;

    private Double allRisk;

    private Integer paymentPeriod;

    private Integer fixedDay;

    private String typeTerm;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate paymentPlanDate;

}
