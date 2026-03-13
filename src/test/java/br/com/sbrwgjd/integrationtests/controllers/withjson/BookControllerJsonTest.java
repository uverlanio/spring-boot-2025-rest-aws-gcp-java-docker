package br.com.sbrwgjd.integrationtests.controllers.withjson;

import br.com.sbrwgjd.config.TestConfigs;
import br.com.sbrwgjd.integrationtests.dto.BooksDTO;
import br.com.sbrwgjd.integrationtests.dto.wrappers.json.WrapperBookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.sql.*;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class BookControllerJsonTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static BooksDTO book;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // Loga o que está indo na request
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // Loga o que está voltando no response
                .build();

        book = new BooksDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {

        mockBook();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(book)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        BooksDTO createdBook = mapper.readValue(content, BooksDTO.class);
        book = createdBook; // para a informação ser reutilizada no próximo teste

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);
        assertEquals("Crônicas de um Futuro Esquecido", createdBook.getTitle());
        assertEquals("Alana V. Kepler", createdBook.getAuthor());
        assertEquals(59.90, createdBook.getPrice());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {

        book.setTitle("O Último Guardião do Orvalho");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(book)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        BooksDTO createdBook = mapper.readValue(content, BooksDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);
        assertEquals("O Último Guardião do Orvalho", createdBook.getTitle());
        assertEquals("Alana V. Kepler", createdBook.getAuthor());
        assertEquals(59.90, createdBook.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("id", book.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        BooksDTO createdBook = mapper.readValue(content, BooksDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId());
        assertTrue(createdBook.getId() > 0);
        assertEquals("O Último Guardião do Orvalho", createdBook.getTitle());
        assertEquals("Alana V. Kepler", createdBook.getAuthor());
        assertEquals(59.90, createdBook.getPrice());
    }

    @Test
    @Order(4)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperBookDTO wrapper = mapper.readValue(content, WrapperBookDTO.class);
        List<BooksDTO> books = wrapper.getEmbeddedDTO().getBooks();
        book = books.get(9);

        assertNotNull(book.getId());
        assertTrue(book.getId() > 0);
        assertEquals("Agile Estimating and Planning", book.getTitle());
        assertEquals("Mike Cohn", book.getAuthor());
        assertEquals(112.87, book.getPrice());
    }

    private void mockBook() {

        book.setTitle("Crônicas de um Futuro Esquecido");
        book.setAuthor("Alana V. Kepler");
        book.setPrice(59.90);
        book.setLaunchDate(new Timestamp(System.currentTimeMillis()));
    }
}