package org.vaadin.example.views.dpf;

import org.vaadin.example.backend.entity.dpf.DpfAccounts;
import org.vaadin.example.backend.entity.dpf.dto.BalanceDpfDto;
import org.vaadin.example.backend.entity.dpf.dto.ProductRateTermAmount;
import org.vaadin.example.backend.service.dpf.BalanceDpfService;
import org.vaadin.example.backend.service.dpf.DpfAccountService;
import org.vaadin.example.backend.service.dpf.ProductRateTermAmountService;
import org.vaadin.example.views.report.FormReportView;
import org.vaadin.example.views.util.PrinterReportJasper;
import org.vaadin.example.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import net.sf.jasperreports.engine.JRException;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

//@Theme(value = Lumo.class, variant = Lumo.DARK)
@Component
public class DpfView {
    @Value("${path_tariff}")
    private String pathTariff;

    @Autowired
    private DpfAccountService dpfAccountService;

    @Autowired
    private BalanceDpfService balanceDpfService;

    @Autowired
    private ProductRateTermAmountService productRateTermAmountService;

    private List<DpfAccounts> dpfAccountsList;

    public VerticalLayout getLayoutDpfAccount(Integer codeClient){
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/dpf.png')" );

        List<ProductRateTermAmount> listParams = productRateTermAmountService.findAll();

        Button btnTariff = new Button(new Image("/buttons/Boton-07.png","Tarifario"));
//        btnTariff.addClassName("button-font-trf");
        btnTariff.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnTariff.addClickListener(click -> {
            Path path = Paths.get(pathTariff+"dpf//dpf.pdf");
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

        Button btnSimulation = new Button(new Image("/buttons/Botones-08.png","Simulacion"));
        btnSimulation.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        btnSimulation.addClassName("button-font-trf");
        btnSimulation.addClickListener(click ->{
            DialogSimulation dialogSimulation = new DialogSimulation(listParams);
            dialogSimulation.open();
        });

        HorizontalLayout header = new HorizontalLayout();
        header.add(btnTariff, btnSimulation);
        header.setSpacing(true);


        VerticalLayout space = new VerticalLayout();

        VerticalLayout space2 = new VerticalLayout();
        space2.setHeight("30px");
        dpfAccountsList = dpfAccountService.getDpfsByClient(codeClient);
        if(dpfAccountsList.size() > 0) {
            space.setHeight("190px");
            layout.add(space, header, space2, createDpfAccountClient());
        }else{
            space.setHeight("290px");
            layout.add(space, header, space2);
        }
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.setSpacing(true);
        layout.setSizeFull();


        return layout;
    }

    private VerticalLayout createDpfAccountClient(){
        VerticalLayout layoutGrid = new VerticalLayout();

//        H2 title = new H2("DEPOPSITOS A PLAZO FIJO");
//        title.addClassName("title-header");

        Grid<DpfAccounts> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);

        grid.setItems(dpfAccountsList);
        grid.addColumn(DpfAccounts::getNumberDpf)
                .setHeader("Numero de DPF")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(DpfAccounts::getProduct)
                .setHeader("Producto")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(DpfAccounts::getRate)
                .setHeader("Tasa")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new LocalDateRenderer<>(DpfAccounts::getExpiredDateConvert, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setHeader("Fecha vencimiento")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(DpfAccounts::getCurrency)
                .setHeader("Moneda")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createAmount))
                .setHeader("Capital")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(DpfAccounts::getTerm)
                .setHeader("Plazo(dias)")
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.addColumn(new ComponentRenderer<>(this::createButtonState))
                .setFlexGrow(0)
                .setAutoWidth(true);
        grid.setHeightByRows(true);

        layoutGrid.add(grid);
        layoutGrid.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layoutGrid.setWidth("70%");
        return layoutGrid;
    }

    private com.vaadin.flow.component.Component createAmount(DpfAccounts dpfAccounts){
        Double balance = dpfAccounts.getAmount();
        return UIUtils.createAmountLabel(balance);
    }

    private com.vaadin.flow.component.Component createButtonState(DpfAccounts dpfAccounts){
        Button btnStateDpf = new Button("Estado DPF");
        btnStateDpf.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnStateDpf.addClickListener(click ->{
            List<BalanceDpfDto> collection = new ArrayList<>();
            BalanceDpfDto balanceDpfDto = balanceDpfService.getBalanceDpf(dpfAccounts.getNumberDpf());
            collection.add(balanceDpfDto);

            InputStream stream = getClass().getResourceAsStream("/template-report/dpf/dpfBalance.jrxml");
            String pathLogo =  getClass().getResource("/template-report/img/logo.png").getPath();
            String pathSubreport ="template-report/dpf/";
            Map<String,Object> params = new WeakHashMap<>();
            params.put("logo",pathLogo);
            params.put("path_subreport", pathSubreport);

            byte[] b = new byte[0];
            try {
                b = PrinterReportJasper.imprimirComoPdf(stream,collection,params);
            } catch (JRException e) {
                e.printStackTrace();
            }
            InputStream is = new ByteArrayInputStream(b);
            try {
                byte[] p = IOUtils.toByteArray(is);
                FormReportView contentReport = new FormReportView("EXTRACTO", p);
                contentReport.open();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return btnStateDpf;
    }

}
