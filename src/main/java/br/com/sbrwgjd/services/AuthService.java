package br.com.sbrwgjd.services;

import br.com.sbrwgjd.data.dto.security.*;
import br.com.sbrwgjd.repository.*;
import br.com.sbrwgjd.security.jwt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(), credentials.getPassword()
                )
        );

        var user = userRepository.findByUsername(credentials.getUsername());

        if(user == null){
            throw new UsernameNotFoundException("Username " + credentials.getUsername()+ " not found!");
        }

        var token = jwtTokenProvider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken){

        var user = userRepository.findByUsername(username);
        TokenDTO token;
        if(user != null) {
            token = jwtTokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(token);
    }
}
