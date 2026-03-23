package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.data.dto.security.*;
import br.com.sbrwgjd.services.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(summary = "Authenticates an user and returns token")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials){

        if(credentialsIsInvalid(credentials)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var token = authService.signIn(credentials);

        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return ResponseEntity.ok().body(token);
    }

    private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null || StringUtils.isBlank(credentials.getUsername()) || StringUtils.isBlank(credentials.getPassword());
    }

}
