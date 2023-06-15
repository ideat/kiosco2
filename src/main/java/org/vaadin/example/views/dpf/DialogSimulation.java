package org.vaadin.example.views.dpf;

import org.vaadin.example.backend.entity.dpf.dto.ProductRateTermAmount;
import org.vaadin.example.views.util.UIUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.*;
import java.util.stream.Collectors;

@CssImport("./styles/my-dialog.css")
public class DialogSimulation extends Dialog {

    public String DOCK = "dock";
    public String FULLSCREEN = "fullscreen";

    private boolean isDocked = false;
    private boolean isFullScreen = false;

    private Header header;
    private Button min;
    private Button max;

    private VerticalLayout content;
    public Footer footer;

    private VerticalLayout layoutResult;
    private List<ProductRateTermAmount> productRateTermAmountList;


    public DialogSimulation(List<ProductRateTermAmount> listParams){

        setDraggable(true);
        setModal(false);
        setResizable(true);
        productRateTermAmountList = listParams;

        layoutResult = new VerticalLayout();

        // Dialog theming
        getElement().getThemeList().add("my-dialog");
        setWidth("800px");

        // Accessibility
        getElement().setAttribute("aria-labelledby", "dialog-title");

        // Header
        H2 title = new H2("Simulación DPF");
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
        footer = new Footer(btnClose);
        add(footer);

        // Button theming
        for (Button button : new Button[] { min, max, close }) {
            button.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        }
    }

    private VerticalLayout layoutSimulation(){

//        ComboBox<String> cmbProduct = new ComboBox<>();
//        cmbProduct.setRequired(true);
//        cmbProduct.setItems("Tarifario DPF");
//        cmbProduct.setWidthFull();
        List<Integer> termSet = new ArrayList<> (new HashSet<> (productRateTermAmountList.stream()
                .map(ProductRateTermAmount::getEndTerm)
                .collect(Collectors.toList())));
        termSet.sort(Comparator.naturalOrder());

        NumberField amount = new NumberField();
        amount.setMin(100.0);
        amount.setStep(50.0);
        amount.setValue(100.0);
        amount.setClearButtonVisible(true);
        amount.setHasControls(true);
        amount.setErrorMessage("Monto invalido");
        amount.setWidthFull();
        amount.setRequiredIndicatorVisible(true);

        Select<String> cmbCurrency = new Select<>();
        cmbCurrency.setItems("Bolivianos","Dolares");
        cmbCurrency.setEmptySelectionAllowed(false);
        cmbCurrency.setValue("Bolivianos");
        cmbCurrency.setWidthFull();

        Select<Integer> term = new Select<>();
        term.setItems(termSet);
        term.setWidthFull();
        term.setEmptySelectionAllowed(false);
        term.setPlaceholder("Seleccione Plazo");

        FormLayout formLayout = new FormLayout();


        HorizontalLayout layoutAmount = new HorizontalLayout();
        layoutAmount.setSpacing(true);
        layoutAmount.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutAmount.setWidthFull();
        layoutAmount.add(amount,cmbCurrency);
        FormLayout.FormItem amountItem = formLayout.addFormItem(layoutAmount,"Monto Inversión:");
        UIUtils.setColSpan(2,amountItem);

        HorizontalLayout layoutTerm = new HorizontalLayout();
        layoutTerm.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutTerm.setWidthFull();
        layoutTerm.add(term);
        FormLayout.FormItem termItem = formLayout.addFormItem(layoutTerm,"Plazo en Días:");
        UIUtils.setColSpan(2, termItem);

        VerticalLayout layout = new VerticalLayout();
        layout.add(formLayout);

        Button btnCalculate = new Button("Calcular");
        btnCalculate.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_LARGE);

        btnCalculate.addClickListener(click -> {
            if(amount.isInvalid()){
                amount.setInvalid(true);
            }
            if(layoutResult.isVisible()){
                layoutResult.removeAll();
            }
            resultLayout(amount.getValue(), term.getValue().intValue(),cmbCurrency.getValue());
            layoutResult.setVisible(true);
        });

        layout.add(btnCalculate, layoutResult );

        return layout;

    }

    private void resultLayout(Double amount,  Integer term, String currency){


        Integer cur = currency.equals("Bolivianos")?1:2;
        List<ProductRateTermAmount> result = productRateTermAmountList.stream()
                .filter(f -> amount.doubleValue() >= f.getInitAmount().doubleValue() &&
                        amount.doubleValue() <=  f.getEndAmount().doubleValue() &&
                        f.getCurrency().equals(cur) &&
                        term.intValue() >= f.getInitTerm().intValue() &&
                        term.intValue() <=  f.getEndTerm().intValue())
                .collect(Collectors.toList());

        Double rate = result.get(0).getRate();
        Double totalInterest = (amount * rate) / 36000 * term;
        Double total = amount + totalInterest;

        H3 labelInterest = new H3();
        labelInterest.setText("Interés ganado ->Tasa:" + rate.toString() + "%");

        H3 labelResult = new H3();
        labelResult.setText("Monto Final (Cap. + Int.):");

        H2 labelResultInterest = new H2();
        String abr = currency.equals("Bolivianos")?"Bs. ":"$us. ";
        labelResultInterest.setText(abr + String.format("%,.2f", totalInterest));

        H2 labelResultTotal = new H2();
        labelResultTotal.setText(abr + String.format("%,.2f", total));

        layoutResult.add(labelInterest);

        layoutResult.add(labelResultInterest);
        layoutResult.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER ,labelResultInterest);

        layoutResult.add(labelResult);

        layoutResult.add(labelResultTotal);
        layoutResult.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER ,labelResultTotal);

        layoutResult.setVisible(false);

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
