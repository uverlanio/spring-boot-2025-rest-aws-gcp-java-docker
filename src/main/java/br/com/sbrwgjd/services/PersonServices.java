package br.com.sbrwgjd.services;

import br.com.sbrwgjd.controllers.PersonController;
import br.com.sbrwgjd.data.dto.PersonDTO;
import br.com.sbrwgjd.exception.BadRequestException;
import br.com.sbrwgjd.exception.FileStorageException;
import br.com.sbrwgjd.exception.RequiredObjectIsNullException;
import br.com.sbrwgjd.exception.ResourceNotFoundException;
import br.com.sbrwgjd.file.importer.contract.FileImporter;
import br.com.sbrwgjd.file.importer.factory.FileImporterFactory;
import br.com.sbrwgjd.model.Person;
import br.com.sbrwgjd.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import static br.com.sbrwgjd.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServices {

    Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository personRepository;
    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;
    @Autowired
    FileImporterFactory importer;

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

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO personDTO) {

        if(personDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");

        var entity = parseObject(personDTO, Person.class);

        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile multipartFile){

        logger.info("Importing People from file!");

        if (multipartFile.isEmpty()) throw new BadRequestException("Please set a valid file!");

        try(InputStream inputStream = multipartFile.getInputStream()) {
            String fileName = Optional.ofNullable(multipartFile.getOriginalFilename()).orElseThrow(
                    () -> new BadRequestException("File name cannot be null!")
            );
            FileImporter importer = this.importer.getFileImporter(fileName);

            List<Person> entities = importer.importFile(inputStream).stream()
                    .map(dto -> personRepository.save(parseObject(dto, Person.class)))
                    .toList();

            return entities.stream()
                    .map(entity -> {
                var dto = parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();

        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
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

        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
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

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
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
