package br.com.sbrwgjd.integrationtests.dto.wrappers.json;

import br.com.sbrwgjd.integrationtests.dto.BooksDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("books")
    private List<BooksDTO> books;

    public BookEmbeddedDTO() {
    }

    public List<BooksDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BooksDTO> books) {
        this.books = books;
    }
}