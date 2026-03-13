package br.com.sbrwgjd.data.dto;

import org.springframework.hateoas.*;
import org.springframework.hateoas.server.core.*;

import java.io.*;
import java.sql.*;
import java.util.*;

@Relation(collectionRelation = "books")
public class BooksDTO extends RepresentationModel<BooksDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String author;

    private Timestamp launchDate;

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

    public Timestamp getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Timestamp launchDate) {
        this.launchDate = launchDate;
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
        BooksDTO booksDTO = (BooksDTO) o;
        return Double.compare(price, booksDTO.price) == 0 && Objects.equals(id, booksDTO.id) && Objects.equals(author, booksDTO.author) && Objects.equals(launchDate, booksDTO.launchDate) && Objects.equals(title, booksDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, author, launchDate, price, title);
    }
}
