package br.com.sbrwgjd.data.dto;

import org.springframework.hateoas.*;

import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class BookDTO extends RepresentationModel<BookDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String author;

    private Timestamp launch_date;

    private double price;

    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(Timestamp launch_date) {
        this.launch_date = launch_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Double.compare(price, bookDTO.price) == 0 && Objects.equals(id, bookDTO.id) && Objects.equals(author, bookDTO.author) && Objects.equals(launch_date, bookDTO.launch_date) && Objects.equals(title, bookDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, author, launch_date, price, title);
    }
}
