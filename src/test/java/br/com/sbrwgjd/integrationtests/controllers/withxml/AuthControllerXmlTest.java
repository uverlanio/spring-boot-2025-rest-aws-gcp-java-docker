package br.com.sbrwgjd.integrationtests.controllers.withxml;

import br.com.sbrwgjd.config.TestConfigs;
import br.com.sbrwgjd.integrationtests.dto.AccountCredentialsDTO;
import br.com.sbrwgjd.integrationtests.dto.TokenDTO;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=8888",
                "spring.datasource.url=jdbc:mysql://localhost:3306/DB_UDEMY?allowPublicKeyRetrieval=true&useSSL=false",
                "spring.datasource.username=root",
                "spring.datasource.password=admin"
        }
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerXmlTest {

    private static TokenDTO tokenDTO;
    private static XmlMapper mapper;

    @BeforeAll
    static void setup() {
        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO  credentials = new AccountCredentialsDTO("Leandro", "admin123");

        var content = given()
                .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                .body(credentials)
                    .when()
                .post()
                    .then()
                .statusCode(200)
                    .extract()
                    .body()
                    .asString();

        tokenDTO = mapper.readValue(content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void refresh() throws JsonProcessingException {

        var content = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParams("username", tokenDTO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                .statusCode(200)
                    .extract()
                    .body()
                    .asString();

        tokenDTO = mapper.readValue(content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}