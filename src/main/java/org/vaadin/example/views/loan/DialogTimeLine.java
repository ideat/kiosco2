package org.vaadin.example.views.loan;

import org.vaadin.example.backend.entity.sec.StageHistoryCreditRequestDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import de.nils_bauer.PureTimeline;
import de.nils_bauer.PureTimelineItem;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@CssImport("./styles/my-dialog.css")
public class DialogTimeLine extends Dialog {

    public String DOCK = "dock";
    public String FULLSCREEN = "fullscreen";

    private boolean isDocked = false;
    private boolean isFullScreen = false;

    private Header header;
    private Button min;
    private Button max;

    private VerticalLayout content;
    public Footer footer;

    private List<StageHistoryCreditRequestDto> stageHistoryCreditRequestDtoList;
    public DialogTimeLine(List<StageHistoryCreditRequestDto> stageHistoryCreditRequestDtos){

        setDraggable(true);
        setModal(false);
        setResizable(true);

        getElement().getThemeList().add("my-dialog");
        setWidth("800px");

        stageHistoryCreditRequestDtoList = stageHistoryCreditRequestDtos;

        // Accessibility
        getElement().setAttribute("aria-labelledby", "dialog-title");

        // Header
        H2 title = new H2("DETALLE ETAPAS DE LA SOLICITUD");
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

        content = new VerticalLayout(createLayoutTimeLine());
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

    private VerticalLayout createLayoutTimeLine(){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("100%");
        PureTimeline timeline = new PureTimeline();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.MEDIUM )
                        .withLocale( Locale.UK )
                        .withZone( ZoneId.systemDefault() );
        timeline.setWidthFull();
        for(StageHistoryCreditRequestDto st: stageHistoryCreditRequestDtoList){

            PureTimelineItem item = new PureTimelineItem(st.getStage()+":"+st.getState().getName());
            String body1 = String.format("Registro: %s"
                    , formatter.format(st.getStartDateTime()));
            String body2 = String.format("Inicio: %s"
                    ,st.getInitDateTime()==null?"":formatter.format(st.getInitDateTime()));
            String body3 = String.format("Fin: %s"
                    ,st.getFinishedDateTime()==null?"":formatter.format(st.getFinishedDateTime()));
//            String hours = String.format("Hrs. asig.: %s Hrs.Trab.: %s", st.getTimeToBeAssigned(),st.getTimeElapsed());
//            String hoursDif = String.format("Hrs. Etapa: %s Hrs. Rest: %s", st.getTotalHoursStage(),
//                    st.getTotalHoursStage()-(st.getTimeElapsed()+st.getTimeToBeAssigned()));
            String userTask = String.format("Usr. etapa: %s", st.getUserTask()!=null? st.getUserTask():"NO ASIGNADO");

            H6 b1 = new H6(body1);
            b1.addClassName("text-color");
            H6 b2 = new H6(body2);
            b2.addClassName("text-color");
            H6 b3 = new H6(body3);
            b3.addClassName("text-color");
            H6 b4 = new H6(userTask);
            b4.addClassName("text-color");


            item.add(b1);
            item.add(b2);
            item.add(b3);
//            item.add(new H6(hours));
//            item.add(new H6(hoursDif));
            item.add(b4);
            timeline.add(item);
        }
        verticalLayout.setSpacing(true);
        verticalLayout.setPadding(true);
        verticalLayout.add(timeline);

        return verticalLayout;
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
