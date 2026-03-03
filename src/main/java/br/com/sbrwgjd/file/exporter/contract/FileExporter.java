package br.com.sbrwgjd.file.exporter.contract;

import br.com.sbrwgjd.data.dto.*;
import org.springframework.core.io.*;

import java.io.*;
import java.util.*;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}
