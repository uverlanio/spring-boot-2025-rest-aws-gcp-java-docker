package br.com.sbrwgjd.services;

import br.com.sbrwgjd.Gender;
import br.com.sbrwgjd.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<Person> findByAll(){

        logger.info("Finding all People!");

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            Person person = mockPerson(i);
            persons.add(person);
        }

        return persons;
    }

    public Person findById(String id){
        logger.info("Finding one Person!");

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Uverlanio");
        person.setLastName("Mauricio");
        person.setAddress("Taboão da Serra - São Paulo - Brasil");
        person.setGender(Gender.Male);

        return person;
    }

    public Person create(Person person) {

        logger.info("Creating one Person!");

        return person;
    }

    public Person update(Person person) {

        logger.info("Updating one Person!");

        return person;
    }

    public void delete(String id) {

        logger.info("Delete one Person!");

    }

    private Person mockPerson(int i) {

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("FirstName " + i);
        person.setLastName("LastName " + i);
        person.setAddress("Some Address in Brasil");
        person.setGender(Gender.Male);

        return person;
    }


}
