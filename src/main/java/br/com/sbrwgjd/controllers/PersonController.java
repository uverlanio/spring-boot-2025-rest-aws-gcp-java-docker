package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.data.dto.PersonDTO;
import br.com.sbrwgjd.mapper.ObjectMapper;
import br.com.sbrwgjd.model.Person;
import br.com.sbrwgjd.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices personServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<PersonDTO> findAll(){

        return ObjectMapper.parseListObjects(personServices.findByAll(), PersonDTO.class);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonDTO findById(@PathVariable("id") Long id){

        var person = personServices.findById(id);

        person.setBirthDay(new Date());
        person.setPhoneNumber("+55 (34) 98765-4321");

        return person;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonDTO create(@RequestBody Person person){
        return personServices.create(person);
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonDTO update(@RequestBody PersonDTO person){

        var p = personServices.findById(person.getId());

        if(Objects.nonNull(p)){
            return personServices.update(person);
        }
        return new PersonDTO();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){

        var p = personServices.findById(id);

        if(Objects.nonNull(p)){
            personServices.delete(p.getId());
        }

        return ResponseEntity.noContent().build();
    }
}
