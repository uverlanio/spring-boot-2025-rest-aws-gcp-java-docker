package br.com.sbrwgjd.integrationtests.dto.wrappers.json;

import br.com.sbrwgjd.integrationtests.dto.*;
import com.fasterxml.jackson.annotation.*;

import java.io.*;
import java.util.*;

public class PersonEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PersonDTO> people;

    public PersonEmbeddedDTO() {}

    public List<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }
}
