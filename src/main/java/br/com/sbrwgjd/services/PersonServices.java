package br.com.sbrwgjd.services;

import br.com.sbrwgjd.data.dto.v1.PersonDTO;
import br.com.sbrwgjd.data.dto.v2.PersonDTOV2;
import br.com.sbrwgjd.exception.ResourceNotFoundException;
import br.com.sbrwgjd.mapper.ObjectMapper;
import br.com.sbrwgjd.mapper.custom.PersonMapper;
import br.com.sbrwgjd.model.Person;
import br.com.sbrwgjd.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonMapper converter;

    public List<Person> findByAll(){

        logger.info("Finding all People!");

        return personRepository.findAll();
    }

    public Person findById(Long id){

        logger.info("Finding one Person!");

        return personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

    }

    public PersonDTO create(Person person) {

        logger.info("Creating one Person!");

        Person p = personRepository.save(person);

        return ObjectMapper.parseObject(p, PersonDTO.class);
    }
    public PersonDTOV2 createV2(PersonDTOV2 person) {

        logger.info("Creating one Person V2!");

        var entity =  converter.convertDTOToEntity(person);

        Person p = personRepository.save(entity);

        return converter.convertEntityToDTO(p);
    }

    public PersonDTO update(PersonDTO person) {

        logger.info("Updating one Person!");

        Person entity = personRepository.findById(person.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

        entity.setFirstName(person.getFirstName());
        entity.setLastName((person.getLastName()));
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        Person p = personRepository.save(entity);

        return ObjectMapper.parseObject(p, PersonDTO.class);
    }

    public void delete(Long id) {

        logger.info("Delete one Person!");

        Person entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

        personRepository.delete(entity);
    }
}
