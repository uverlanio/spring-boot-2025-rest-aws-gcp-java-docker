package br.com.sbrwgjd.file.importer.impl;

import br.com.sbrwgjd.data.dto.*;
import br.com.sbrwgjd.file.importer.contract.*;
import org.apache.commons.csv.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

public class XlsxImporter implements FileImporter {
    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        try(XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0); // 1a aba da planilha
            Iterator<Row> rowIterator = sheet.iterator();

            if(rowIterator.hasNext()) { // pular a primeira linha da planilha
                rowIterator.next();
            }
            return parseRowsToPersonDtoList(rowIterator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<PersonDTO> parseRowsToPersonDtoList(Iterator<Row> rowIterator) {

        List<PersonDTO> people = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if(isRowValid(row)){
                people.add(parseRowToPersonDto(row));
            }
        }
        return people;
    }

    private PersonDTO parseRowToPersonDto(Row row) {

        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName(row.getCell(0).getStringCellValue());
        personDTO.setLastName(row.getCell(1).getStringCellValue());
        personDTO.setAddress(row.getCell(2).getStringCellValue());
        personDTO.setGender(row.getCell(3).getStringCellValue());
        personDTO.setEnabled(true);
        return personDTO;
    }

    private static boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
