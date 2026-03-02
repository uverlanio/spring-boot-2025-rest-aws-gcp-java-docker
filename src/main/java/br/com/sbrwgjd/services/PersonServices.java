package br.com.sbrwgjd.services;

import br.com.sbrwgjd.controllers.PersonController;
import br.com.sbrwgjd.data.dto.PersonDTO;
import br.com.sbrwgjd.exception.RequiredObjectIsNullException;
import br.com.sbrwgjd.exception.ResourceNotFoundException;
import br.com.sbrwgjd.mapper.ObjectMapper;
import br.com.sbrwgjd.model.Person;
import br.com.sbrwgjd.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PersonServices {

    Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository personRepository;
    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PagedModel<EntityModel<PersonDTO>> findByAll(Pageable pageable){

        logger.info("Finding all People!");

        var people = personRepository.findAll(pageable);

        return buildPagedModel(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable){

        logger.info("Finding People by name!");

        var people = personRepository.findPeopleByName(firstName, pageable);

        return buildPagedModel(pageable, people);
    }

    public PersonDTO findById(Long id){

        logger.info("Finding one Person!");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var dto = ObjectMapper.parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO personDTO) {

        if(personDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");

        var entity = ObjectMapper.parseObject(personDTO, Person.class);

        var dto = ObjectMapper.parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO personDTO) {

        if(personDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");

        Person entity = personRepository.findById(personDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

        entity.setFirstName(personDTO.getFirstName());
        entity.setLastName((personDTO.getLastName()));
        entity.setAddress(personDTO.getAddress());
        entity.setGender(personDTO.getGender());

        var dto = ObjectMapper.parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {

        logger.info("Delete one Person!");

        Person entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

        personRepository.delete(entity);
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {

        logger.info("Disabling one Person!");

        personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

        personRepository.disablePerson(id);

        var entity = personRepository.findById(id).orElse(new  Person());

        var dto = ObjectMapper.parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = ObjectMapper.parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = linkTo(
                methodOn(PersonController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                String.valueOf(pageable.getSort())
                        )).withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1,12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("",1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("findAll").withType("DELETE"));
    }
}
