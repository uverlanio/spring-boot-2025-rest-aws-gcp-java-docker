package br.com.sbrwgjd.controllers.docs;

import br.com.sbrwgjd.data.dto.security.*;
import io.swagger.v3.oas.annotations.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {

    @Operation(summary = "Authenticates an user and returns token")
    ResponseEntity<?> signin(AccountCredentialsDTO credentials);

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    ResponseEntity<?> refresh(String username, String refreshToken);

    AccountCredentialsDTO create(AccountCredentialsDTO credentials);
}
