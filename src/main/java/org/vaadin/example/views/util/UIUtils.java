package org.vaadin.example.views.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UIUtils {

    public static Label createAmountLabel(double amount) {
        Label label = createH5Label(formatAmount(amount));
        label.addClassName(LumoStyles.FontFamily.MONOSPACE);
        return label;
    }

    public static Label createH5Label(String text) {
        Label label = new Label(text);
        label.addClassName(LumoStyles.Heading.H5);
        return label;
    }

    public static void setColSpan(int span, Component... components) {
        for (Component component : components) {
            component.getElement().setAttribute("colspan",
                    Integer.toString(span));
        }
    }

    public static void setTextColor(TextColor textColor, Component... components) {
        for (Component component : components) {
            component.getElement().getStyle().set("color", textColor.getValue());
        }
    }

    public static Icon createPrimaryIcon(VaadinIcon icon) {
        Icon i = new Icon(icon);
        setTextColor(TextColor.PRIMARY, i);
        return i;
    }
    public static Icon createSecondaryIcon(VaadinIcon icon) {
        Icon i = new Icon(icon);
        setTextColor(TextColor.SECONDARY, i);
        return i;
    }

    public static Icon createErrorIcon(VaadinIcon icon) {
        Icon i = new Icon(icon);
        setTextColor(TextColor.ERROR, i);
        return i;
    }

    public static String formatAmount(Double amount) {
        return decimalFormat.get().format(amount);
    }

    private static final ThreadLocal<DecimalFormat> decimalFormat = ThreadLocal
            .withInitial(() -> new DecimalFormat("###,###.00", DecimalFormatSymbols.getInstance(Locale.US)));
    private static final ThreadLocal<DateTimeFormatter> dateFormat = ThreadLocal
            .withInitial(() -> DateTimeFormatter.ofPattern("dd MMM, YYYY"));
}
