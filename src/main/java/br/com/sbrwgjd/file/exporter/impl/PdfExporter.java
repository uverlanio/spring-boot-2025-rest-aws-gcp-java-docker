package br.com.sbrwgjd.file.exporter.impl;

import br.com.sbrwgjd.data.dto.PersonDTO;
import br.com.sbrwgjd.file.exporter.contract.FileExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Component
public class PdfExporter implements FileExporter {
    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/templates/people.jrxml");
        if(inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/people.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);

        Map<String,Object> parameters = new HashMap<>();
        //parameters.put("title", "People Report");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
