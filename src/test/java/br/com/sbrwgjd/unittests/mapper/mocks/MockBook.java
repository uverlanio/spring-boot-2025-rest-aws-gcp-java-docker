package br.com.sbrwgjd.unittests.mapper.mocks;

import br.com.sbrwgjd.data.dto.*;
import br.com.sbrwgjd.model.*;

import java.sql.*;
import java.time.*;
import java.util.*;

public class MockBook {

    public Books mockEntity() {
        return mockEntity(0);
    }
    
    public BookDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Books> mockEntityList() {
        List<Books> books = new ArrayList<Books>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }
    
    public Books mockEntity(Integer number) {
        Books book = new Books();
        book.setId(Long.valueOf(number));
        book.setTitle("O Hobbit"+number);
        book.setAuthor("J.R.R. Tolkien"+number);
        book.setLaunch_date(Timestamp.from(Instant.now()));
        book.setPrice(125.50);
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setTitle("O Hobbit"+number);
        book.setAuthor("J.R.R. Tolkien"+number);
        book.setLaunch_date(Timestamp.from(Instant.now()));
        book.setId(number.longValue());
        book.setPrice(125.50);
        return book;
    }
}