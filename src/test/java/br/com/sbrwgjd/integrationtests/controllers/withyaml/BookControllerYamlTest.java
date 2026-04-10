package br.com.sbrwgjd.integrationtests.controllers.withyaml;

import br.com.sbrwgjd.config.*;
import br.com.sbrwgjd.integrationtests.controllers.withyaml.mapper.*;
import br.com.sbrwgjd.integrationtests.dto.*;
import br.com.sbrwgjd.integrationtests.dto.wrappers.xmlandyaml.*;
import com.fasterxml.jackson.core.*;
import io.restassured.builder.*;
import io.restassured.config.*;
import io.restassured.filter.log.*;
import io.restassured.http.*;
import io.restassured.specification.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;

import java.sql.*;
import java.util.*;

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
class BookControllerYamlTest {

    private static RequestSpecification specification;
    private static YAMLMapper mapper;
    private static BooksDTO book;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        mapper = new YAMLMapper();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // Loga o que está indo na request
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // Loga o que está voltando no response
                .build();

        book = new BooksDTO();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(0)
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("Leandro", "admin123");

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

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // Loga o que está indo na request
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // Loga o que está voltando no response
                .build();

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {

        mockBook();

        var createdBook = given().config
                        (RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                MediaType.APPLICATION_YAML_VALUE,
                                                ContentType.TEXT))
                        )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                    .body(book, mapper)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(BooksDTO.class, mapper);
        
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);
        assertEquals("Cronicas de um Futuro Esquecido", createdBook.getTitle());
        assertEquals("Alana V. Kepler", createdBook.getAuthor());
        assertEquals(59.90, createdBook.getPrice());

    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {

        book.setTitle("O Ultimo Guardiao do Orvalho");

        var createdBook = given().config
                        (RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                MediaType.APPLICATION_YAML_VALUE,
                                                ContentType.TEXT))
                        )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                    .body(book, mapper)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(BooksDTO.class, mapper);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);
        assertEquals("O Ultimo Guardiao do Orvalho", createdBook.getTitle());
        assertEquals("Alana V. Kepler", createdBook.getAuthor());
        assertEquals(59.90, createdBook.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var createdBook = given().config
                        (RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                MediaType.APPLICATION_YAML_VALUE,
                                                ContentType.TEXT))
                        )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParams("id", book.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(BooksDTO.class, mapper);

        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);
        assertEquals("O Ultimo Guardiao do Orvalho", createdBook.getTitle());
        assertEquals("Alana V. Kepler", createdBook.getAuthor());
        assertEquals(59.90, createdBook.getPrice());
    }

    @Test
    @Order(4)
    void findAllTest() throws JsonProcessingException {

        var content = given().config
                        (RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(
                                                MediaType.APPLICATION_YAML_VALUE,
                                                ContentType.TEXT))
                        )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PagedModelBook.class, mapper);

        List<BooksDTO> books = content.getContent();
        book = books.get(9);

        assertNotNull(book.getId());
        assertTrue(book.getId() > 0);
        assertEquals("Agile Estimating and Planning", book.getTitle());
        assertEquals("Mike Cohn", book.getAuthor());
        assertEquals(112.87, book.getPrice());
    }

    private void mockBook() {

        book.setTitle("Cronicas de um Futuro Esquecido");
        book.setAuthor("Alana V. Kepler");
        book.setPrice(59.90);
        book.setLaunchDate(new Timestamp(System.currentTimeMillis()));
    }
}