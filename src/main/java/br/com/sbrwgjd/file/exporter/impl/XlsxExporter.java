package br.com.sbrwgjd.file.exporter.impl;

import br.com.sbrwgjd.data.dto.PersonDTO;
import br.com.sbrwgjd.file.exporter.contract.PersonExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class XlsxExporter implements PersonExporter {
    @Override
    public Resource exportPeople(List<PersonDTO> people) throws Exception {

        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("People");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID","First Name","Last Name","Address","Gender","Enabled"};
            for(int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            int rowIndex = 1;
            for(PersonDTO personDTO : people) {
                Row row = sheet.createRow(rowIndex++);
                createRowWithPeople(personDTO, row);
            }

            for(int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        return null;
    }

    private static void createRowWithPeople(PersonDTO personDTO, Row row) {
        row.createCell(0).setCellValue(personDTO.getId());
        row.createCell(1).setCellValue(personDTO.getFirstName());
        row.createCell(2).setCellValue(personDTO.getLastName());
        row.createCell(3).setCellValue(personDTO.getAddress());
        row.createCell(4).setCellValue(personDTO.getGender());
        row.createCell(5).setCellValue(personDTO.getEnabled() != null && personDTO.getEnabled() ? "Yes" : "No");
    }

    private CellStyle createHeaderStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }
}
