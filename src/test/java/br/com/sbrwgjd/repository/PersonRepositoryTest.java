package br.com.sbrwgjd.repository;

import br.com.sbrwgjd.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(
        properties = {
                "server.port=8888",
                "spring.datasource.url=jdbc:mysql://localhost:3306/DB_UDEMY?allowPublicKeyRetrieval=true&useSSL=false",
                "spring.datasource.username=root",
                "spring.datasource.password=admin"
        }
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest {

    @Autowired
    PersonRepository repository;

    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(
                0,
                12,
                Sort.by(Sort.Direction.ASC, "firstName"));

        person = repository.findPeopleByName("Amand", pageable).getContent().get(0);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Amanda", person.getFirstName());
        assertEquals("Gomes", person.getLastName());
        assertEquals("Feliz Natal - MT", person.getAddress());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {

        Long id = person.getId();
        repository.disablePerson(id);

        var result = repository.findById(id);

        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Amanda", person.getFirstName());
        assertEquals("Gomes", person.getLastName());
        assertEquals("Feliz Natal - MT", person.getAddress());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }
}