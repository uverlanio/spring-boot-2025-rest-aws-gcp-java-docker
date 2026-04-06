package br.com.sbrwgjd.integrationtests.controllers.withyaml;

import br.com.sbrwgjd.config.*;
import br.com.sbrwgjd.integrationtests.controllers.withyaml.mapper.*;
import br.com.sbrwgjd.integrationtests.dto.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.*;
import io.restassured.config.*;
import io.restassured.http.*;
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
class AuthControllerYamlTest {

    private static TokenDTO tokenDTO;
    private static YAMLMapper mapper;

    @BeforeAll
    static void setup() {
        mapper = new YAMLMapper();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO  credentials = new AccountCredentialsDTO("Leandro", "admin123");

        tokenDTO = given().config(
                RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                MediaType.APPLICATION_YAML_VALUE,
                                                ContentType.TEXT))
                        )
                .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(credentials, mapper)
                    .when()
                .post()
                    .then()
                .statusCode(200)
                    .extract()
                    .body()
                    .as(TokenDTO.class, mapper);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void refresh() throws JsonProcessingException {

        tokenDTO = given().config(
                RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                MediaType.APPLICATION_YAML_VALUE,
                                                ContentType.TEXT))
                        )
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .pathParams("username", tokenDTO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                .statusCode(200)
                    .extract()
                    .body()
                .as(TokenDTO.class, mapper);


        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}