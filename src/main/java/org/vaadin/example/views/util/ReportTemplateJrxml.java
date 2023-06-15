package org.vaadin.example.views.util;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReportTemplateJrxml {

    private Collection<?> datasource;
    private JasperReport report;
    private JRDataSource jrDataSource;
    Map<String,Object> params = new HashMap<>();

    private ReportTemplateJrxml(){

    }

    public ReportTemplateJrxml(InputStream path, Collection<?> datasource, Map<String,Object> params) throws JRException {
        this.datasource = datasource;
        this.jrDataSource = new JRBeanCollectionDataSource(datasource);
        params.put("logo","template-report/img/logo.png");
        this.params = params;
        this.report = JasperCompileManager.compileReport(path);
    }

    public JasperPrint getReport() throws JRException {
        JasperPrint print = JasperFillManager.fillReport(report,params,jrDataSource);
        return print;
    }

    public OutputStream getReportStream() throws JRException {
        OutputStream output = null;
        JasperFillManager.fillReportToStream(report,output,params);
        return output;
    }


}
