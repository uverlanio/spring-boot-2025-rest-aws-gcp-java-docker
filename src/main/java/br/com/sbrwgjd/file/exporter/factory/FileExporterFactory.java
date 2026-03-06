package br.com.sbrwgjd.file.exporter.factory;

import br.com.sbrwgjd.exception.*;
import br.com.sbrwgjd.file.exporter.*;
import br.com.sbrwgjd.file.exporter.contract.*;
import br.com.sbrwgjd.file.exporter.impl.*;
import br.com.sbrwgjd.file.importer.contract.*;
import br.com.sbrwgjd.file.importer.impl.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;

@Component
public class FileExporterFactory {

    private Logger logger  = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) {
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        }else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE)) {
            return context.getBean(PdfExporter.class);
        } else {
            throw new BadRequestException("Invalid file format");
        }
    }
}
