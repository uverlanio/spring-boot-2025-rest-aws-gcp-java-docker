package br.com.sbrwgjd.services;

import br.com.sbrwgjd.Gender;
import br.com.sbrwgjd.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    Logger logger = Logger.getLogger(PersonServices.class.getName());

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
}
