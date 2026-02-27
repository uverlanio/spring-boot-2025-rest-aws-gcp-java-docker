package br.com.sbrwgjd.file.importer.factory;

import br.com.sbrwgjd.exception.*;
import br.com.sbrwgjd.file.importer.contract.*;
import br.com.sbrwgjd.file.importer.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileImporterFactory {

    private Logger logger  = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileImporter getFileImporter(String fileName) {
        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        } else {
            throw new BadRequestException("Invalid file format");
        }
    }
}
