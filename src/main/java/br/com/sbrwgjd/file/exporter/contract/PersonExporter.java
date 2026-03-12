package br.com.sbrwgjd.file.exporter.contract;

import br.com.sbrwgjd.data.dto.*;
import org.springframework.core.io.*;

import java.util.*;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws Exception;
    Resource exportPerson(PersonDTO person) throws Exception;
}
