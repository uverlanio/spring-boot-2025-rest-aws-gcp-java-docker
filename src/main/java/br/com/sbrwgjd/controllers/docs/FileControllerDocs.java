package br.com.sbrwgjd.controllers.docs;

import br.com.sbrwgjd.data.dto.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.util.*;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {

    UploadFileResponseDTO uploadFile(MultipartFile file) throws IOException;
    List<UploadFileResponseDTO> uploadFiles(MultipartFile[] files) throws IOException;
    ResponseEntity<ResponseEntity> downloadFile(String fileName, HttpServletRequest request);

}
