package br.com.sbrwgjd.services;

import br.com.sbrwgjd.data.dto.*;
import br.com.sbrwgjd.data.dto.security.*;
import br.com.sbrwgjd.exception.*;
import br.com.sbrwgjd.model.*;
import br.com.sbrwgjd.model.User;
import br.com.sbrwgjd.repository.*;
import br.com.sbrwgjd.security.jwt.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

import static br.com.sbrwgjd.mapper.ObjectMapper.*;
import static br.com.sbrwgjd.mapper.ObjectMapper.parseObject;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials){
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(), credentials.getPassword()
                )
        );
        } catch (RuntimeException e) {
            throw new InvalidJwtAuthenticationException(e.getMessage());
        }

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

    public AccountCredentialsDTO create(AccountCredentialsDTO user) {

        if(user == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one new User!");

        var entity = new User();

        toCreateNewUser(user, entity);
        var dto = userRepository.save(entity);
        return new AccountCredentialsDTO(dto.getUsername(), dto.getPassword(), dto.getFullName());

    }

    private void toCreateNewUser(AccountCredentialsDTO user, User entity) {

        entity.setUsername(user.getUsername());
        entity.setFullName(user.getFullname());
        entity.setPassword(generateHashPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);
    }

    private String generateHashPassword(String password) {

        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
                "",
                8,
                185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("pbkdf2"));

        return passwordEncoder.encode(password);
    }
}
