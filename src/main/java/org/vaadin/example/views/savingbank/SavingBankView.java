package org.vaadin.example.views.savingbank;


import org.vaadin.example.backend.entity.savingBank.SavingBankClient;
import org.vaadin.example.backend.service.savingBank.BalanceSavingBankDtoService;
import org.vaadin.example.backend.service.savingBank.SavingBankService;
import org.vaadin.example.views.report.FormReportView;
import org.vaadin.example.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class SavingBankView extends VerticalLayout {

    @Value("${path_tariff}")
    private String pathTariff;

    @Autowired
    private SavingBankService savingBankService;

    @Autowired
    private BalanceSavingBankDtoService balanceSavingBankDtoService;

    private List<SavingBankClient> savingBankClientList;



    public VerticalLayout getLayoutSavingBank(Integer codeClient){
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/savingbank.png')" );


        Button btnTariff = new Button(new Image("/buttons/Boton-07.png","Tarifario"));
        btnTariff.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        btnTariff.addClassName("button-font-trf");

        VerticalLayout header = new VerticalLayout();
        header.add(btnTariff);
//        header.setAlignContent(FlexLayout.ContentAlignment.START);
//        header.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        header.setAlignItems(Alignment.BASELINE);
//        header.setSizeFull();

        btnTariff.addClickListener(click -> {
            Path path = Paths.get(pathTariff+"ahorro//ahorro.pdf");
            try {
                byte[] bFile = Files.readAllBytes(path);
                InputStream is = new ByteArrayInputStream(bFile);
                byte[] p = IOUtils.toByteArray(is);
                FormReportView contentReport = new FormReportView("TARIFARIO", p);
                contentReport.open();
            }catch (IOException e) {
                e.printStackTrace();
            }
        });

        VerticalLayout space = new VerticalLayout();
        space.setHeight("200px");

        savingBankClientList = savingBankService.getSavingBankClient(codeClient);
        VerticalLayout lSavingBank = createSavingBankClient();
        layout.add(space,header, lSavingBank);
        layout.setAlignItems(Alignment.BASELINE);
        layout.setHorizontalComponentAlignment(Alignment.BASELINE,lSavingBank);

        layout.setSizeFull();



        return layout;
    }



    private VerticalLayout createSavingBankClient(){
        VerticalLayout layoutGrid = new VerticalLayout();
//
//        H2 title = new H2("CAJAS DE AHORRO");
//        title.addClassName("title-header");

        Grid<SavingBankClient> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

        grid.setItems(savingBankClientList);
        grid.addColumn(SavingBankClient::getAccount)
                .setHeader("Numero de Cuenta")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getProductName)
                .setHeader("Producto")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getRate)
                .setHeader("Tasa")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getState)
                .setHeader("Estado")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(SavingBankClient::getCurrency)
                .setHeader("Moneda")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createBalance))
                .setHeader("Saldo")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createButtonSavingBank))
                .setFlexGrow(0)
                .setAutoWidth(true);

        grid.setHeightByRows(true);
        grid.setWidth("55%");

        layoutGrid.add(grid);
        layoutGrid.setHorizontalComponentAlignment(Alignment.AUTO);
        layoutGrid.setWidth("90%");
        return layoutGrid;
    }

    private com.vaadin.flow.component.Component createBalance(SavingBankClient savingBankClient){
        Double balance = savingBankClient.getBalance();
        return UIUtils.createAmountLabel(balance);
    }

    private com.vaadin.flow.component.Component createButtonSavingBank(SavingBankClient savingBankClient){
        Button btnSavingBank = new Button("Extracto");
        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        Button btnBalance = new Button("Saldo");
//        btnBalance.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
//        btnBalance.setIcon(VaadinIcon.PRINT.create());

        HorizontalLayout layout = new HorizontalLayout();
//        layout.add(btnBalance,btnSavingBank);
        layout.add(btnSavingBank);
        layout.setSpacing(true);

        btnSavingBank.addClickListener(click -> {
           DiaglogExtract diaglogExtract = new DiaglogExtract(savingBankClient.getAccount(), balanceSavingBankDtoService);
           diaglogExtract.open();
        });

//        btnBalance.addClickListener(click -> {
//
//        });

        return layout;
    }



}
