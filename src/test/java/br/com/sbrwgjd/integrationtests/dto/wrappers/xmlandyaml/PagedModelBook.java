package br.com.sbrwgjd.integrationtests.dto.wrappers.xmlandyaml;

import br.com.sbrwgjd.integrationtests.dto.*;
import jakarta.xml.bind.annotation.*;

import java.io.*;
import java.util.*;

@XmlRootElement
public class PagedModelBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<BooksDTO> content;

    public PagedModelBook() { }

    public List<BooksDTO> getContent() {
        return content;
    }

    public void setContent(List<BooksDTO> content) {
        this.content = content;
    }
}
