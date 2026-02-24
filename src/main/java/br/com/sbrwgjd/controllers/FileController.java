package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.controllers.docs.*;
import br.com.sbrwgjd.data.dto.*;
import br.com.sbrwgjd.services.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.support.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api/file/v1")
public class FileController implements FileControllerDocs {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService service;

    @Override
    @PostMapping("/uploadFile")
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        var fileName = service.storeFile(file);
        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/")
                .path(fileName)
                .toUriString();
        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @Override
    public List<UploadFileResponseDTO> uploadFiles(MultipartFile[] files) throws IOException {
        return List.of();
    }

    @Override
    public ResponseEntity<ResponseEntity> downloadFile(String fileName, HttpServletRequest request) {
        return null;
    }
}
