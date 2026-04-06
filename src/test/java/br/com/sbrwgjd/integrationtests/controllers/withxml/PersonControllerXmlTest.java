package br.com.sbrwgjd.integrationtests.controllers.withxml;

import br.com.sbrwgjd.config.TestConfigs;
import br.com.sbrwgjd.integrationtests.dto.*;
import br.com.sbrwgjd.integrationtests.dto.wrappers.xmlandyaml.PagedModelPerson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
class PersonControllerXmlTest {

    private static RequestSpecification specification;
    private static XmlMapper mapper;
    private static PersonDTO person;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
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
                .setBasePath("/api/person/v1")
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

        mockPerson();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = mapper.readValue(content, PersonDTO.class);
        person = createdPerson; // para a informação ser reutilizada no próximo teste

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {

        person.setLastName("Benedict Torvalds");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .body(person)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = mapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = mapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", person.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = mapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = mapper.readValue(content, PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();
        person = people.get(9);

        assertNotNull(person.getId());
        assertTrue(person.getId() > 0);
        assertEquals("Adelaida", person.getFirstName());
        assertEquals("Renard", person.getLastName());
        assertEquals("Room 717", person.getAddress());
        assertEquals("Female", person.getGender());
        assertFalse(!person.getEnabled());
    }

    @Test
    @Order(5)
    void findByNameTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParams("firstName", "and")
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("findPeopleByName/{firstName}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = mapper.readValue(content, PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();
        person = people.get(9);

        assertNotNull(person.getId());
        assertTrue(person.getId() > 0);
        assertEquals("Brandie", person.getFirstName());
        assertEquals("Mengue", person.getLastName());
        assertEquals("Suite 36", person.getAddress());
        assertEquals("Female", person.getGender());
        assertFalse(!person.getEnabled());
    }

    /*@Test
    @Order(6)
    void delete() throws JsonProcessingException {

        given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParams("id", person.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }
*/
    private void mockPerson() {

        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
        person.setProfileUrl("https://profile.google.com");
        person.setPhotoUrl("https://avatars.githubusercontent.com");
    }
}