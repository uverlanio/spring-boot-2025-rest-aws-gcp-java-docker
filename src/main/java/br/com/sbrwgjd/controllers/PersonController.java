package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.model.Person;
import br.com.sbrwgjd.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    private PersonServices personServices;

    @GetMapping
    public List<Person> findAll(){
        return personServices.findByAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findById(@PathVariable("id") String id){
        return personServices.findById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person create(@RequestBody Person person){
        return personServices.create(person);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person update(@RequestBody Person person){

        Person p = personServices.findById(person.getId().toString());

        if(Objects.nonNull(p)){
            return personServices.update(person);
        }
        return new Person();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){

        Person p = personServices.findById(id);

        if(Objects.nonNull(p)){
            personServices.delete(p.getId().toString());
        }
    }
}
