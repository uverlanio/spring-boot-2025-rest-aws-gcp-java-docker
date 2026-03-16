package br.com.sbrwgjd.services;

import br.com.sbrwgjd.config.*;
import br.com.sbrwgjd.data.dto.request.*;
import br.com.sbrwgjd.mail.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import java.io.*;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfigs;

    public void sendSimplesEmail(EmailRequestDTO emailRequestDTO){
        emailSender
                .to(emailRequestDTO.getTo())
                .withubject(emailRequestDTO.getSubject())
                .withMessage(emailRequestDTO.getBody())
                .send(emailConfigs);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment){

        File tempFile = null;

        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender
                    .to(emailRequestDTO.getTo())
                    .withubject(emailRequestDTO.getSubject())
                    .withMessage(emailRequestDTO.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfigs);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment", e);
        } finally {
            if(tempFile != null && tempFile.exists()){
                tempFile.delete();
            }
        }
    }
}
