package br.com.sbrwgjd.controllers.docs;

import br.com.sbrwgjd.data.dto.security.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {

    @Operation(summary = "Authenticates an user and returns token",
            description = "Authenticates an user and returns token",
            tags = "Authentication",
            responses = {
                @ApiResponse(
                    description = "Success",
                        responseCode = "200",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AccountCredentialsDTO.class)
                                    )
                        }
                ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> signin(AccountCredentialsDTO credentials);

    @Operation(summary = "Refresh token for authenticated user and returns a token",
    description = "Refresh token for authenticated user and returns a token",
    tags = "Refresh",
    responses = {
        @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = AccountCredentialsDTO.class)
                        )
                }
        ),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    }
    )
    ResponseEntity<?> refresh(String username, String refreshToken);

    AccountCredentialsDTO create(AccountCredentialsDTO credentials);
}
