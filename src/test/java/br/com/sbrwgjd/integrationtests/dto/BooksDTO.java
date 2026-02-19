package br.com.sbrwgjd.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@XmlRootElement
public class BooksDTO implements Serializable{

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
        BooksDTO booksDTO = (BooksDTO) o;
        return Double.compare(price, booksDTO.price) == 0 && Objects.equals(id, booksDTO.id) && Objects.equals(author, booksDTO.author) && Objects.equals(launch_date, booksDTO.launch_date) && Objects.equals(title, booksDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, launch_date, price, title);
    }
}
