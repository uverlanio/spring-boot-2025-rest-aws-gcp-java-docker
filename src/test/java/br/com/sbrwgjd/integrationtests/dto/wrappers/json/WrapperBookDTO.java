package br.com.sbrwgjd.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.*;

import java.io.*;

public class WrapperBookDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private BookEmbeddedDTO embeddedDTO;

    public WrapperBookDTO() {}

    public BookEmbeddedDTO getEmbeddedDTO() {
        return embeddedDTO;
    }

    public void setEmbeddedDTO(BookEmbeddedDTO embeddedDTO) {
        this.embeddedDTO = embeddedDTO;
    }
}
