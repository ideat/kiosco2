package org.vaadin.example.views.verifyIdCard;

import org.vaadin.example.backend.entity.autoform.DataIdCard;
import org.vaadin.example.backend.entity.autoform.Forms;
import org.vaadin.example.backend.entity.autoform.Parameter;
import org.vaadin.example.backend.entity.autoform.Service;
import org.vaadin.example.backend.rest.AutoFormService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.wontlost.sweetalert2.Config;
import com.wontlost.sweetalert2.SweetAlert2Vaadin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class VerifyIdCardView {

    @Autowired
    private AutoFormService autoFormService;

    public VerticalLayout getLayoutVerifyIdCard(Integer codeClient){

        Button btnCreateRequest = new Button(new Image("/buttons/Botones-11.png","Solicitar Verificacion Datos"));
        btnCreateRequest.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnCreateRequest.setWidthFull();
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background-image","url('/backgrounds/verifyidcard.png')" );

        VerticalLayout space = new VerticalLayout();
        space.setHeight("300px");

        HorizontalLayout layout2 = new HorizontalLayout();
        HorizontalLayout spaceH = new HorizontalLayout();
        spaceH.setWidth("300px");
        layout2.add(spaceH,btnCreateRequest);
        layout2.setAlignItems(FlexComponent.Alignment.CENTER);
//        layout2.setWidth("400px");
        layout.add(space,layout2);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.START,layout2);
        layout.setSizeFull();

        btnCreateRequest.addClickListener(click -> {

            Forms newForm = new Forms();
            newForm.setIdClient(codeClient);
            newForm.setNameTypeForm("VERIF. SEGIP");
            newForm.setCategoryTypeForm("VARIOS");

//            SimpleDateFormat formatterDate = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();//(Date) VaadinSession.getCurrent().getAttribute("current-date");

            newForm.setCreationDate(currentDate);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date dateHour = new Date();
            String hour = formatter.format(dateHour);
            newForm.setCreationTime(hour);

            newForm.setIdCardForVerification(createDataIdCardVerification());
            autoFormService.create(newForm);
//            Notification notification = new Notification();
//            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
//            Span label = new Span("Solicitud creada, firme el documento");
//            notification.add(label);
//            notification.setDuration(6000);
//            notification.setPosition(Notification.Position.MIDDLE);
//            notification.open();

            Config config = new Config();
            config.setTitle("Información");
            config.setText("Solicitud creada, firme el documento");
            config.setIcon("info");

            new SweetAlert2Vaadin(config).open();

        });

        return layout;
    }

    private String createDataIdCardVerification(){
        DataIdCard dataIdCard = new DataIdCard();

        List<DataIdCard> dataIdCardList = new ArrayList<>();
        String typePerson = VaadinSession.getCurrent().getAttribute("type-person").toString();

        if(typePerson.equals("1")){
            String idCard = VaadinSession.getCurrent().getAttribute("idcard").toString();

            dataIdCard.setNumberId(idCard);
            dataIdCard.setExtension(idCard.substring(idCard.length()-2,idCard.length()));
        }else{
            dataIdCard.setNumberId( VaadinSession.getCurrent().getAttribute("nit").toString());
        }

        dataIdCard.setFullName(VaadinSession.getCurrent().getAttribute("fullname").toString());

        dataIdCardList.add(dataIdCard);
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try {
            result = mapper.writeValueAsString(dataIdCardList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;

    }


}
