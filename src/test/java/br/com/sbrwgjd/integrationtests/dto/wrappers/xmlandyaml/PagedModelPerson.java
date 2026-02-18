package br.com.sbrwgjd.integrationtests.dto.wrappers.xmlandyaml;

import br.com.sbrwgjd.integrationtests.dto.*;
import jakarta.xml.bind.annotation.*;

import java.io.*;
import java.util.*;

@XmlRootElement
public class PagedModelPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    public List<PersonDTO> content;

    public PagedModelPerson() { }

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
}
