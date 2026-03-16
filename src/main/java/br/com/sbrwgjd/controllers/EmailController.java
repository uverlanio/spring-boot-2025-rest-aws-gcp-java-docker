package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.controllers.docs.*;
import br.com.sbrwgjd.data.dto.request.*;
import br.com.sbrwgjd.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        emailService.sendSimplesEmail(emailRequestDTO);
        return new ResponseEntity<>("e-Mail sent with success!", HttpStatus.OK);
    }

    @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachament(
            @RequestParam("emailRequest") String emailRequestJson,
            @RequestParam("attachment") MultipartFile multipartFile) {

        emailService.sendEmailWithAttachment(emailRequestJson, multipartFile);
        return new ResponseEntity<>("e-Mail with attachment sent successfully", HttpStatus.OK);
    }
}
