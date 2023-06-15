package org.vaadin.example.views.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class PrinterReportJasper {

    public static byte[] imprimirComoPdf(InputStream istreams, Collection<?> datasources, Map<String, Object> params) throws JRException {
        Objects.requireNonNull(istreams,"'istreams' no puede ser omitido");
        Objects.requireNonNull(datasources,"'datasources' no puede ser omitido");

        ReportTemplateJrxml report = new ReportTemplateJrxml(istreams,datasources,params);


        JasperPrint jp = new JasperPrint();
        jp = report.getReport();
        return getReportPdf(jp);

    }

    public static byte[] getReportPdf(final JasperPrint jp)  {
        try {
            return JasperExportManager.exportReportToPdf(jp);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }

    }
}
