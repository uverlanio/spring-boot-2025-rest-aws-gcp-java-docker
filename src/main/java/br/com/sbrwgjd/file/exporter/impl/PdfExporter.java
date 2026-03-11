package br.com.sbrwgjd.file.exporter.impl;

import br.com.sbrwgjd.data.dto.PersonDTO;
import br.com.sbrwgjd.file.exporter.contract.FileExporter;
import br.com.sbrwgjd.services.QRCodeService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements FileExporter {

    @Autowired
    private QRCodeService service;

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
        if(inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/people.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);

        Map<String,Object> parameters = new HashMap<>();
        //parameters.put("title", "People Report");

        jasperReport.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
        if(mainTemplateStream == null) {
            throw new RuntimeException("Template file not found: /templates/person.jrxml");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
        if(subReportStream == null) {
            throw new RuntimeException("Template file not found: /templates/books.jrxml");
        }

        InputStream qrCodeStream = service.generateQRCode(person.getProfileUrl(), 200, 200);

        // 1. Compile os relatórios
        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // 2. Prepare o DataSource do Subreport (Lista de livros)
        // person.getBooks() deve retornar uma Collection
        JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(person.getBooks());

        // 3. Prepare os parâmetros
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PERSON_ID", person.getId());
        parameters.put("SUB_REPORT_DATA_SOURCE", subDataSource);
        parameters.put("BOOK_SUB_REPORT", subReport); // Objeto compilado
        parameters.put("QR_CODEIMAGE", qrCodeStream); // Se for usar como imagem

        // 4. DataSource Principal
        // Se você quer usar os dados do objeto 'person' que já está na memória:
        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));

        // 5. Preencher (Fill)
        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainDataSource);

        String path = getClass().getResource("/templates/books.jasper").getPath();

        mainReport.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
