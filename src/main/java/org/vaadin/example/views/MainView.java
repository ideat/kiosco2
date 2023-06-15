package org.vaadin.example.views;

import org.vaadin.example.backend.service.client.IdimgService;
import org.vaadin.example.views.debitCard.DebitCardView;
import org.vaadin.example.views.digitalbank.DigitalBankView;
import org.vaadin.example.views.dpf.DpfView;
import org.vaadin.example.views.loan.LoanView;
import org.vaadin.example.views.report.FormReportView;
import org.vaadin.example.views.savingbank.SavingBankView;
import org.vaadin.example.views.verifyIdCard.VerifyIdCardView;
import com.flowingcode.vaadin.addons.carousel.Carousel;
import com.flowingcode.vaadin.addons.carousel.Slide;
import com.vaadin.componentfactory.IdleNotification;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

//import de.mekaso.vaadin.addons.Carousel;

@PageTitle("Opciones")
@Route("")
@RouteAlias("kiosco")
@CssImport("./styles/styles.css")
//@PWA(name = "Kiosco UI", shortName = "Kiosco UI", iconPath = "images/logo-18.png", backgroundColor = "#5074a4", themeColor = "#5074a4")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainView extends VerticalLayout implements  RouterLayout, HasUrlParameter<String> { //, SessionInitListener, SessionDestroyListener {

    private static final String BACKGROUND = "hsla(%s, 100%%, 50%%, 0.8)";

    @Value("${path_tariff}")
    private String pathTariff;

    @Value("${url_digital_bank}")
    private String urlDigitalBank;

    @Value("${url_open_account}")
    private String urlOpenAccount;

    @Autowired
    private SavingBankView savingBankView;

    @Autowired
    private DpfView dpfView;

    @Autowired
    private LoanView loanView;

    @Autowired
    private DigitalBankView digitalBankView;

    @Autowired
    private DebitCardView debitCardView;

    @Autowired
    private VerifyIdCardView verifyIdCardView;

    @Autowired
    private IdimgService idimgService;

    private Button btnSavingBank;
    private Button btnDpf;
    private Button btnLoan;
    private Button btnDebitCard;
    private Button btnDigitalBank;
    private Button btnPersonalData;
    private Button btnOpenAccount;

    private Button exit;

    private Carousel carousel;

    private Map<String, List<String>> param;
    private Integer codeClient;
    private Image image;

    public MainView() {

//       this.getElement().getStyle().set("background-image","url('background.jpg')");
        this.getElement().getStyle().set("background","whitesmoke");

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent,  @OptionalParameter String s) {
        VaadinSession.getCurrent()
                .getSession()
                .setMaxInactiveInterval(100);

        IdleNotification idleNotification = new IdleNotification();

        // No. of secs before timeout, at which point the notification is displayed
        idleNotification.setSecondsBeforeNotification(20);
        idleNotification.setMessage("Su sesión expirara en " +
                IdleNotification.MessageFormatting.SECS_TO_TIMEOUT
                + " segundos.");
        idleNotification.addExtendSessionButton("CONTINUAR");
        idleNotification.setExtendSessionOnOutsideClick(true);
        idleNotification.addRedirectButton("SALIR", "expired");
        idleNotification.setRedirectAtTimeoutUrl("expired");

        idleNotification.addCloseButton();
        idleNotification.setExtendSessionOnOutsideClick(false);

        idleNotification.addDetachListener(event -> {
            if(UI.getCurrent().isClosing()) {
                VaadinSession.getCurrent().setAttribute("fullname",null);
                VaadinSession.getCurrent().setAttribute("name",null);
                VaadinSession.getCurrent().setAttribute("idcard",null);
                VaadinSession.getCurrent().setAttribute("type-person",null);
                VaadinSession.getCurrent().setAttribute("nit",null);
                VaadinSession.getCurrent().setAttribute("codeclient",null);

//                UI.getCurrent().getPage().executeJs("javascript:window.close('','_parent','');");

            }
        });
        Div divNotif = new Div();
        VerticalLayout v = new VerticalLayout();
        v.setHeight("50px");
        divNotif.add(v);
        divNotif.addClassName("title-header");

        if(!UI.getCurrent().isClosing()) {
            UI.getCurrent().add(divNotif,idleNotification);

            Location location = beforeEvent.getLocation();
            QueryParameters qp = location.getQueryParameters();
            param = qp.getParameters();
            VaadinSession.getCurrent().setAttribute("fullname", param.get("fullname").get(0));
            VaadinSession.getCurrent().setAttribute("name", param.get("name").get(0));
            VaadinSession.getCurrent().setAttribute("idcard", param.get("idcard").get(0));
            VaadinSession.getCurrent().setAttribute("type-person", param.get("type-person").get(0));
            VaadinSession.getCurrent().setAttribute("nit", param.get("nit").get(0));
            VaadinSession.getCurrent().setAttribute("codeclient", param.get("codeclient").get(0));

            codeClient = Integer.parseInt(param.get("codeclient").get(0));
        }

    }
    
    @Override
    protected void onAttach(AttachEvent attachEvent){
        Logger logger = Logger.getLogger(MainView.class.getName());
        super.setAlignItems(Alignment.CENTER);
        super.setHeightFull();
        DeploymentConfiguration deployConf = VaadinSession.getCurrent().getConfiguration();
        int hbi =deployConf.getHeartbeatInterval();
        boolean killIdle = deployConf.isCloseIdleSessions();
        logger.info(String.format("Deployment Config >> KillIdleSessions : %s -- HeartBeatInterval : %s", killIdle, hbi));

//        carousel = Carousel.create();
//        carousel.setWidth("1600px");
//        carousel.setHeight("800px");

        //Layout SavingBank
        VerticalLayout layoutSavingBank = createSimpleDiv(5);
        layoutSavingBank.add(savingBankView.getLayoutSavingBank(codeClient));
        layoutSavingBank.setSizeFull();
        layoutSavingBank.setAlignItems(Alignment.BASELINE);


        //Layout DPF
        VerticalLayout layoutDpf = createSimpleDiv(5);
        layoutDpf.add(dpfView.getLayoutDpfAccount(codeClient));
        layoutDpf.setAlignItems(Alignment.END);
        layoutDpf.setSizeFull();

        //Layout Loan
        VerticalLayout layoutLoan = createSimpleDiv(5);
        layoutLoan.setSizeFull();
        layoutLoan.add(loanView.getLayoutLoanAccounts(codeClient));
        layoutLoan.setAlignItems(Alignment.END);

        VerticalLayout layoutMenu = createSimpleDiv(5);

        //Digital Bank
        VerticalLayout layoutDigitalBank = createSimpleDiv(5);
        layoutDigitalBank.add(digitalBankView.getLayoutDigitalBank(codeClient,urlDigitalBank));
        layoutDigitalBank.setSizeFull();
        layoutDigitalBank.setAlignItems(Alignment.START);

        //Debit Card
        VerticalLayout layoutDebitCard = createSimpleDiv(5);
        layoutDebitCard.add(debitCardView.getLayoutDebitCard(codeClient));
        layoutDebitCard.setSizeFull();
        layoutDebitCard.setAlignItems(Alignment.END);

        //Verification IdCard
        VerticalLayout layoutVerifyIdCard = createSimpleDiv(5);
        layoutVerifyIdCard.add(verifyIdCardView.getLayoutVerifyIdCard(codeClient));
        layoutVerifyIdCard.setSizeFull();
        layoutVerifyIdCard.setAlignItems(Alignment.START);

        layoutMenu.add(menuLayout());
//        carousel.add(layoutMenu);
//        carousel.add(layoutSavingBank);
//        carousel.add(layoutDpf);
//        carousel.add(layoutLoan);
//        carousel.add(layoutDigitalBank);
//        carousel.add(layoutDebitCard);
//        carousel.add(layoutVerifyIdCard);

        Slide slideMenu = new Slide(layoutMenu);
        Slide slideSavingBank = new Slide(layoutSavingBank);
        Slide slideDpf = new Slide(layoutDpf);
        Slide slideLoan = new Slide(layoutLoan);
        Slide slideDigitalBank = new Slide(layoutDigitalBank);
        Slide slideDebitCard = new Slide(layoutDebitCard);
        Slide slideVerifyIdCard = new Slide(layoutVerifyIdCard);

        carousel = new Carousel(slideMenu,slideSavingBank,slideDpf,slideLoan,slideDigitalBank,slideDebitCard,slideVerifyIdCard)
                .withSlideDuration(1000);
        carousel.setWidth("1600px");
        carousel.setHeight("810px");
        carousel.setHideNavigation(true);
        carousel.setDisableSwipe(true);

//        carousel.add(new Button("CANCELAR"));


        Button home = new Button(new Image("/buttons/Botones-00.png","Menu Principal"));
        home.setWidth("380px");
        home.setHeight("100px");
        home.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        home.addClickListener(click -> {
//            Component targetComponent = carousel.getChildren().collect(Collectors.toList()).get(0);
//            carousel.show(targetComponent);
//            carousel.show(0);
//            if(carousel.getSelectedIndex()!=0){
//                carousel.show(0);
//            }
            carousel.movePos(0);
        });

        exit = new Button(new Image("/buttons/Botones-20.png","Menu Principal"));
        exit.setWidth("380px");
        exit.setHeight("100px");
        exit.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        exit.addClickListener(click -> {
            UI.getCurrent().getPage().executeJs("javascript:window.close('','_parent','');");

        });

        Button btnTariff = new Button(new Image("/buttons/Botones-13.png","Tarifario completo"));
        btnTariff.setWidth("380px");
        btnTariff.setHeight("100px");
        btnTariff.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnTariff.addClickListener(click -> {

            Path path = Paths.get(pathTariff+"completo//tarifario_completo.pdf");
            try {
                byte[] bFile = Files.readAllBytes(path);
                InputStream is = new ByteArrayInputStream(bFile);
                byte[] p = IOUtils.toByteArray(is);
                FormReportView contentReport = new FormReportView("TARIFARIO COMPLETO", p);
                contentReport.open();
            }catch (IOException e) {
                e.printStackTrace();
            }

        });

        HorizontalLayout header = new HorizontalLayout();
        header.add(home,btnTariff,exit);

        add(header,carousel);

//        setAlignContent(ContentAlignment.CENTER);

        super.getStyle().set("justify-content", "space-evenly");
        super.getStyle().set("flex-direction", "column");
    }

    private HorizontalLayout menuLayout(){
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout optionLayout = layoutOptions();
        HorizontalLayout space = new HorizontalLayout();
        space.setWidth("90px");
//        Integer codeClient = Integer.parseInt(param.get("codeclient").get(0));
//        byte[] imageBytes = idimgService.getImageClient(codeClient);
//        StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes));
//        image = new Image(resource,"picture");
//        image.setWidth("200px");
//        image.setHeight("200px");
//        image.addClassName("rounded-corners");
//
//        VerticalLayout layoutImg = new VerticalLayout();
//        layoutImg.add(image);
//        layoutImg.setAlignItems(Alignment.START);
        layout.add(layoutInfoClient(),space,optionLayout);
        layout.setSpacing(true);
        layout.setVerticalComponentAlignment(Alignment.END,optionLayout);
        layout.setAlignItems(Alignment.CENTER);
        layout.getStyle().set("background", "whitesmoke");
        layout.getElement().getStyle().set("background-image","url('/backgrounds/menu.png')");
        layout.setSizeFull();

        return layout;
    }

    private VerticalLayout layoutOptions(){
//        Div space = new Div();
//        space.setHeight("13px");
//        Div space1 = new Div();
//        space1.setHeight("13px");
//        Div space2 = new Div();
//        space2.setHeight("13px");
//        Div space3 = new Div();
//        space3.setHeight("13px");
        btnSavingBank = new Button(new Image("/buttons/Botones-01.png","Caja Ahorro"));

//        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        btnSavingBank.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnSavingBank.setWidth("380px");
        btnSavingBank.setHeight("100px");

//        btnSavingBank.addClickListener(click -> carousel.show(1));
        btnSavingBank.addClickListener(click -> carousel.movePos(1));

        btnDpf = new Button(new Image("/buttons/Botones-02.png","DPF"));
        btnDpf.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnDpf.setWidth("380px");
        btnDpf.setHeight("100px");
//        btnDpf.addClassName("button-font");
        btnDpf.addClickListener(click -> carousel.movePos(2));
        
        btnLoan = new Button(new Image("/buttons/Botones-03.png","Caja Ahorro"));
        btnLoan.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnLoan.setWidth("380px");
        btnLoan.setHeight("100px");
//        btnLoan.addClassName("button-font");
        btnLoan.addClickListener(click -> carousel.movePos(3));

        btnDebitCard = new Button(new Image("/buttons/Botones-04.png","Apertura cuenta"));
        btnDebitCard.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnDebitCard.setWidth("380px");
        btnDebitCard.setHeight("100px");
        btnDebitCard.addClickListener(click -> carousel.movePos(5));
//        btnDebitCard.addClassName("button-font");
        
        btnDigitalBank = new Button(new Image("/buttons/Botones-05.png","Caja Ahorro"));
        btnDigitalBank.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnDigitalBank.setWidth("380px");
        btnDigitalBank.setHeight("100px");
//        btnDigitalBank.addClassName("button-font");
        btnDigitalBank.addClickListener(click -> carousel.movePos(4));


        btnOpenAccount = new Button(new Image("/buttons/Botones-15.png","Caja Ahorro"));
        btnOpenAccount.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnOpenAccount.setWidth("380px");
        btnOpenAccount.setHeight("100px");
//        btnDigitalBank.addClassName("button-font");
        btnOpenAccount.addClickListener(click -> {
            String u = String.format( "window.open(\"%s\", \"Apertura de cuenta\", \"top=200,left=500,width=950,height=700\")",urlOpenAccount);
            UI.getCurrent().getPage().executeJs(u);
//            UI.getCurrent().getPage().executeJs(" window.open(\"https://web.bankingly.com/Administration.WebUI/Pages/General/Login.aspx?ID=LaPromotora\", \"Apertura de cuenta\", \"top=200,left=500,width=950,height=700\")");
        });

        btnPersonalData = new Button(new Image("/buttons/Botones-06.png","Caja Ahorro"));
        btnPersonalData.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnPersonalData.setWidth("380px");
        btnPersonalData.setHeight("100px");
        btnPersonalData.addClickListener(click -> carousel.movePos(6));
//        btnPersonalData.addClassName("button-font");

        VerticalLayout layout1 = new VerticalLayout();
        layout1.add(btnSavingBank, btnDpf, btnLoan, btnDebitCard, btnDigitalBank, btnOpenAccount);
//        layout1.getStyle().set("background", "whitesmoke");
//        layout1.add(btnSavingBank,space, btnDpf, space1,btnLoan, space2, btnDebitCard, space3,btnDigitalBank);
        layout1.setAlignItems(Alignment.END);

        return layout1;
        
    }

    private Component layoutInfoClient(){
        String name = VaadinSession.getCurrent().getAttribute("name").toString();
//        Html intro = new Html(" <H4  style=\"padding-top: 50px; padding-right: 20px; padding-bottom: 20px; padding-left: 500px\"> <p style=\"color:#0D416C;\">Bienvenido </p> </H4> ");
        Html client = new Html( String.format( " <p style=\"font-size:55px; padding-top: 0px; padding-right: 0px; padding-bottom: 152px; padding-left: 280px; color:#004F82;\"> <b>  %s </b> </p>" ,name));
//
//        Html productivity = new Html("<p> <b>Kiosco una manera fácil y conveniente de realizar tus consultas de saldo y estado de cuenta, " +
//                "efectuar transferencias entre cuentas nacionales, " +
//                "pagar tus facturas y obtener información sobre nuestros productos y servicios." +
//                "En el Kiosco encontrarás la calculadora financiera para modelar tu próximo crédito y calcular el rendimiento de tus inversiones futuras.</b></p>");
        Div space = new Div();
        space.setHeight("1px");
        space.setMaxHeight("1px");

        Integer codeClient = Integer.parseInt(param.get("codeclient").get(0));
        Optional<byte[]> imageBytes = idimgService.getImageClient(codeClient);
        StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(imageBytes.get()));
        image = new Image(resource,"picture");
        image.setWidth("270px");
        image.setHeight("270px");
        image.addClassName("rounded-corners");


        HorizontalLayout spaceH = new HorizontalLayout();
        spaceH.setWidth("745px");

        HorizontalLayout layoutImg = new HorizontalLayout();
        layoutImg.add(spaceH,image);

        VerticalLayout spaceV = new VerticalLayout();
        spaceV.setHeight("53px");

        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.add(client);

        infoLayout.setHorizontalComponentAlignment(Alignment.CENTER,client);
//        infoLayout.setHorizontalComponentAlignment(Alignment.CENTER,client);
        infoLayout.setWidth("940px");
        VerticalLayout layout = new VerticalLayout();
//        layout.setFlexDirection(FlexDirection.COLUMN);
        layout.add(spaceV,layoutImg,infoLayout);
        layout.setMaxWidth("1600px");
        layout.setMaxHeight("800px");
        layout.addClassName("text-color");
        layout.setAlignItems(Alignment.START);

        return layout;
    }

    private VerticalLayout createSimpleDiv(Integer value) {
        VerticalLayout content = new VerticalLayout();
        int tempcolor = value * 40;
        String background = String.format(BACKGROUND, tempcolor);
        content.getStyle().set("background", "whitesmoke");
//        content.add(new H1(String.valueOf(value)));
        return content;
    }


//    @Override
//    public Element getElement() {
//        return null;
//    }
//
//    @Override
//    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {
//        VaadinSession session = sessionDestroyEvent.getSession();
//        if(VaadinSession.getCurrent().getAttribute("name")==null){
//            UI.getCurrent().getPage().executeJs("javascript:window.close('','_parent','');");
//        }
//    }
//
//    @Override
//    public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {
//
//    }
}
