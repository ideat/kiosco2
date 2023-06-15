package org.vaadin.example.backend.entity.sec.creditRequest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreditRequest {
    private UUID id;

    private Integer numberRequest;

    @Positive(message = "'Monto' debe ser positivo")
    private Double amount;

    @Positive(message = "'Tasa Interes' debe ser positivoa")
    private Double rateInterest;

    @NotNull(message = "'Moneda' es requerida")
    private String currency;

    @Positive(message = "'Plazo' debe ser positivo")
    private int term;

    @NotNull(message = "'Tipo de plazo es requerido'")
    private String typeTerm;

    @PositiveOrZero(message = "'Periodo de pago' debe ser positivo")
    private int paymentPeriod;

    @Min(value = 0, message = "Valor minimo para dia fijo es 0")
    @Max(value = 30, message = "Valos maximo para dia fijo de pago es 30")
    private int fixedDay;

    @NotNull(message = "'Tipo de cuota' es requerida")
    private String typeFee;

    private Double baseInterestRate;

    private Integer initPeriodBaseRate;

    @NotNull(message = "'CAEDEC del destino del credito' es requerido")
    private String caedec;

    @NotNull(message = "'Estado de la solicitud' es requerido")
    private String state;

//    @NotNull(message = "'Ubicacion de la solicitud' es requerida")
//    private String location;

//    @NotNull(message = "'Fecha de solicitud' es requerida")
    private LocalDate requestDate;

    @NotNull
    private LocalDate paymentPlanDate;

    @NotNull(message = "'Destino del credito' es requerido")
    private String destination;

    private String charge;

    private String linkup ;

    @NotNull(message = "Tipo de credito es requerido")
    private String typeCredit;

    @NotNull(message = "Producto de credito es requerido")
    private String productCredit;

    @NotNull(message = "Objeto de credito es requerido")
    private String objectCredit;

    private String loginUser;

    private int idOffice;

    private String stateHistory;

    private String savingBox;

    private int hoursSpend;

    private int daysSpend;

    private Integer numberCredit;

    private String typeGuarantee;

    private String companySizeIndicator;

    private String operationType;

    private Integer gracePeriod;

    private String typeGracePeriod;

    private String noOwnGuarantee;//json

    private LocalDate paymentPlanEndDate;
}
