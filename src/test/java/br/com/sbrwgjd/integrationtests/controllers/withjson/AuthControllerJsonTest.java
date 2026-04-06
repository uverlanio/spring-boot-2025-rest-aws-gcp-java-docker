package br.com.sbrwgjd.integrationtests.controllers.withjson;

import br.com.sbrwgjd.config.*;
import br.com.sbrwgjd.integrationtests.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;

import static io.restassured.RestAssured.*;
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
class AuthControllerJsonTest {

    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setup() {
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() {
        AccountCredentialsDTO  credentials = new AccountCredentialsDTO("Leandro", "admin123");

        tokenDTO = given()
                .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                    .when()
                .post()
                    .then()
                .statusCode(200)
                    .extract()
                    .body()
                    .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void refresh() {

        tokenDTO = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParams("username", tokenDTO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                .statusCode(200)
                    .extract()
                    .body()
                    .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}