package br.com.sbrwgjd.controllers.docs;

import br.com.sbrwgjd.data.dto.request.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.http.*;
import org.springframework.web.multipart.*;

public interface EmailControllerDocs {

    @Operation(summary = "Send an e-Mail",
            description = "Sends an e-mail by providing details, subject and body!",
            tags = {"e-Mail"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(summary = "Send an e-Mail with Attachment",
            description = "Sends an e-mail with Attachment by providing details, subject and body!",
            tags = {"e-Mail"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<String> sendEmailWithAttachament(String emailRequestJson, MultipartFile multipartFile);
}
