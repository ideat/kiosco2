package org.vaadin.example.views.loan;

import org.vaadin.example.backend.entity.sec.creditRequest.PaymentPlanSimulation;
import org.vaadin.example.backend.entity.sec.kiosco.ProductKiosco;
import org.vaadin.example.backend.entity.sec.creditRequest.CreditRequest;
import org.vaadin.example.backend.rest.SecService;
import org.vaadin.example.views.report.FormReportView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import com.wontlost.sweetalert2.Config;
import com.wontlost.sweetalert2.SweetAlert2Vaadin;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CssImport("./styles/my-dialog.css")
public class DialogPaymentPlanSimulation extends Dialog {
    public String DOCK = "dock";
    public String FULLSCREEN = "fullscreen";

    private boolean isDocked = false;
    private boolean isFullScreen = false;

    private Header header;
    private Button min;
    private Button max;

    private VerticalLayout content;
    public Footer footer;

    private Binder<PaymentPlanSimulation> binder;
    private PaymentPlanSimulation paymentPlanSimulation;

    private Select<String> concept;
    private Select<String> product;
    private NumberField amount;
    private IntegerField term;

    static Integer maxTerm = 1;
    static Double maxA = 1.0;
    static Double minA = 1.0;
    private List<String> productList;
    private Set<String> conceptSet;
    private List<ProductKiosco> productKioscoGlobalList;
    private ProductKiosco aux;


    public DialogPaymentPlanSimulation(List<ProductKiosco> productKioscoList, SecService secService){

        setDraggable(true);
        setModal(false);
        setResizable(true);
        productKioscoGlobalList = productKioscoList;
        conceptSet = new HashSet<>(productKioscoList.stream()
                .map(ProductKiosco::getConcept)
                .collect(Collectors.toList()));


        getElement().getThemeList().add("my-dialog");
        setWidth("800px");

        // Accessibility
        getElement().setAttribute("aria-labelledby", "dialog-title");

        // Header
        H2 title = new H2("SIMULACION PLAN DE PAGOS");
        title.addClassName("dialog-title");

        min = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        min.addClickListener(event -> minimise());

        max = new Button(VaadinIcon.EXPAND_SQUARE.create());
        max.addClickListener(event -> maximise());

        //////////////////

        Button close = new Button(VaadinIcon.CLOSE_BIG.create());
        close.addClickListener(event -> close());

        header = new Header(title,  close);
        header.getElement().getThemeList().add(Lumo.DARK);
        add(header);

        content = new VerticalLayout(layoutSimulation());
        content.addClassName("dialog-content");
        content.setAlignItems(FlexComponent.Alignment.STRETCH);
        add(content);

        // Footer
        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        btnClose.addClickListener(event -> close());

        Button btnSimulation = new Button("Simulación");
        btnSimulation.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        btnSimulation.addClickListener(click -> {
            if(binder.writeBeanIfValid(paymentPlanSimulation)){
                paymentPlanSimulation.setTypeFee("FIJA");
                paymentPlanSimulation.setTypeTerm("MES");
                paymentPlanSimulation.setPaymentPeriod(30);
                LocalDate currentDate = LocalDate.now();
                paymentPlanSimulation.setFixedDay(currentDate.getDayOfMonth());
                paymentPlanSimulation.setPaymentPlanDate(currentDate);
                paymentPlanSimulation.setFullName(VaadinSession.getCurrent().getAttribute("fullname").toString());

                ObjectMapper mapper = new ObjectMapper();
                String ps = "";
                try {
                    ps = mapper.writeValueAsString(paymentPlanSimulation);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                byte[] r = secService.getPaymentPlanSimulationReport(ps);
                InputStream is = new ByteArrayInputStream(r);

                try {
                    byte[] report = IOUtils.toByteArray(is);
                    FormReportView contentReport = new FormReportView("SIMULACION PLAN PAGOS", report);
                    contentReport.open();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                Config config = new Config();
                config.setTitle("Error");
                config.setText("Complete la informacion requerida");
                config.setIcon("error");

                new SweetAlert2Vaadin(config).open();
            }
        });

        footer = new Footer(btnSimulation,btnClose);
        add(footer);

        // Button theming
        for (Button button : new Button[] { min, max, close }) {
            button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        }
    }

    private VerticalLayout layoutSimulation(){
        VerticalLayout layout = new VerticalLayout();
        paymentPlanSimulation = new PaymentPlanSimulation();
        binder = new Binder<>();

        concept = new Select<>();
        concept.setRequiredIndicatorVisible(true);
        concept.setEmptySelectionCaption("Seleccione un Destino");
        concept.setLabel("Destino");
        concept.setItems(conceptSet);
        concept.setWidthFull();
        concept.setEmptySelectionAllowed(true);
        concept.addComponents(null,new Hr());
        concept.addValueChangeListener(selector -> {
            productList = productKioscoGlobalList.stream()
                    .filter(value -> value.getConcept().equals(selector.getValue()))
                    .map(ProductKiosco::getProduct)
                    .collect(Collectors.toList());
            product.clear();
            product.setItems(productList);
            amount.clear();
            term.clear();

        });


        product = new Select<>();
        product.setRequiredIndicatorVisible(true);
        product.setLabel("Producto");
        product.setItems();

        product.setPlaceholder("Seleccione un Producto");
        product.setEmptySelectionCaption("Seleccione un Producto");
        product.setEmptySelectionAllowed(true);

        // add a divider after the empty selection item
        product.addComponents(null,new Hr());
        product.setWidthFull();
        product.addValueChangeListener(selector -> {
            if(selector.getValue()!=null) {
                ProductKiosco productKiosco = productKioscoGlobalList.stream()
                        .filter(value -> value.getConcept().equals(concept.getValue()) && value.getProduct().equals(selector.getValue()))
                        .collect(Collectors.toList()).get(0);
                maxTerm = productKiosco.getTerm();
                maxA = productKiosco.getMaxAmount();
                minA = productKiosco.getMinAmount();
                term.setMax(maxTerm);
                amount.setMax(maxA);
                amount.setMin(minA);
                aux = productKiosco;
                binder.validate();
                paymentPlanSimulation.setRate(productKiosco.getRate());
                paymentPlanSimulation.setAllRisk(productKiosco.getAllRisk());
                paymentPlanSimulation.setSecure(productKiosco.getSecure());

            }
        });


        term = new IntegerField("Plazo(meses)");
        term.setHasControls(true);
        term.setStep(1);
        term.setMin(1);
//        term.setErrorMessage("Maximo " + maxTerm);
//        term.setMax(maxTerm);
        term.setWidthFull();



        amount = new NumberField("Monto solicitado");
        amount.setHasControls(true);
        amount.setStep(1000);
//        amount.setMin(minA);
//        amount.setMax(maxA);
        amount.setPrefixComponent(symbolCurrencyBs());
        amount.setWidthFull();
//        amount.addValueChangeListener(event -> binder.validate());
        Label termStatus = new Label();
        termStatus.addClassName("text-status");
        Label amountStatus = new Label();
        amountStatus.addClassName("text-status");

        binder.forField(concept)
                .asRequired("Destino de credito es requerido")
                .withValidator(value -> !value.equals("Seleccione un Destino"),"Debe seleccionar el destino")
                .bind(PaymentPlanSimulation::getDestiny,PaymentPlanSimulation::setDestiny);
        binder.forField(product)
                .asRequired("Producto de credito es requerido")
                .withValidator(value -> !value.equals("Seleccione un Producto"),"Debe seleccionar un producto")
                .bind(PaymentPlanSimulation::getProductName,PaymentPlanSimulation::setProductName);
        binder.forField(amount)
                .asRequired("Monto del credito es requerido")
                .withValidator(value -> value>minA,"uno")
                .withValidator(value -> value <= maxA, "dos")
                .withValidationStatusHandler(status -> {
                    Optional<String> text = Optional.empty();
                    if(status.getMessage().isPresent()){
                        if(status.getMessage().get().equals("uno")){
                            text = Optional.ofNullable("Monto debe ser mayor a " + minA);
                        }else if(status.getMessage().get().equals("dos")){
                            text = Optional.ofNullable("Monto deber ser menor o igual a " + maxA);
                        }
                    }
                    amountStatus.setText(text.orElse(""));
                    amountStatus.setVisible(status.isError());
                })
                .bind(PaymentPlanSimulation::getAmount, PaymentPlanSimulation::setAmount);
        binder.forField(term)
                .asRequired("Plazo del credito es requerido")
                .withValidator(value -> value>1,"uno" )
                .withValidator(value -> value <= term.getMax(), "dos")
                .withValidationStatusHandler(status -> {
                    Optional<String> text = Optional.empty();
                    if(status.getMessage().isPresent()) {
                        if (status.getMessage().get().equals("uno")) {
                            text = Optional.ofNullable("Plazo debe ser mayor a 1");
                        } else if (status.getMessage().get().equals("dos")) {
                            text = Optional.ofNullable("Plazo maximo para el producto es de: " + maxTerm + " meses");
                        }
                    }
                    termStatus.setText(text.orElse(""));
                    termStatus.setVisible(status.isError());
                })
                .bind(PaymentPlanSimulation::getTerm,PaymentPlanSimulation::setTerm);

        layout.add(concept,product,term,termStatus,amount,amountStatus);

        return layout;
    }

    private Component symbolCurrencyBs(){
        Label symbol = new Label("Bs.");

        return symbol;
    }

    private void minimise() {
        if (isDocked) {
            initialSize();
        } else {
            if (isFullScreen) {
                initialSize();
            }
            min.setIcon(VaadinIcon.UPLOAD_ALT.create());
            getElement().getThemeList().add(DOCK);
            setWidth("620px");
        }
        isDocked = !isDocked;
        isFullScreen = false;
        content.setVisible(!isDocked);
//        footer.setVisible(!isDocked);
    }

    private void initialSize() {
        min.setIcon(VaadinIcon.DOWNLOAD_ALT.create());
        getElement().getThemeList().remove(DOCK);
        max.setIcon(VaadinIcon.EXPAND_SQUARE.create());
        getElement().getThemeList().remove(FULLSCREEN);
        setHeight("auto");
        setWidth("600px");
    }

    private void maximise() {
        if (isFullScreen) {
            initialSize();
        } else {
            if (isDocked) {
                initialSize();
            }
            max.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
            getElement().getThemeList().add(FULLSCREEN);
            setSizeFull();
            content.setVisible(true);
//            footer.setVisible(true);
        }
        isFullScreen = !isFullScreen;
        isDocked = false;
    }
}

