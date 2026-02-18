package br.com.sbrwgjd.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.*;

import java.io.*;

public class WrapperPersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO embeddedDTO;

    public WrapperPersonDTO() {}

    public PersonEmbeddedDTO getEmbeddedDTO() {
        return embeddedDTO;
    }

    public void setEmbeddedDTO(PersonEmbeddedDTO embeddedDTO) {
        this.embeddedDTO = embeddedDTO;
    }
}
