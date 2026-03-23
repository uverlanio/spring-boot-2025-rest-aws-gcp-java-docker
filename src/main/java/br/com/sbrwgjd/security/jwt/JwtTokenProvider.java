package br.com.sbrwgjd.security.jwt;

import br.com.sbrwgjd.data.dto.security.*;
import br.com.sbrwgjd.exception.*;
import com.auth0.jwt.*;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.interfaces.*;
import jakarta.annotation.*;
import jakarta.servlet.http.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.support.*;

import java.util.*;

@Service
public class JwtTokenProvider{

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-lenght:3600000}")
    private long validityInMilliSeconds;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAccessToken(String username, List<String> roles){

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliSeconds);
        String accessToken = getAccessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);
        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date refreshTokenValidity = new Date(now.getTime() + validityInMilliSeconds * 3);
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenValidity)
                .withSubject(username)
                .sign(algorithm);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/oauth/token").build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;

    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring("Bearer ".length());
        } return null;
    }

    public Boolean validateToken(String token){
        DecodedJWT decodedJWT = decodedToken(token);

        try {
            if(decodedJWT.getExpiresAt().before(new Date())){
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token!");
        }

    }

}
