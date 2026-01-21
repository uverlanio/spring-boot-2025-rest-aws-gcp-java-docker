package br.com.sbrwgjd.services;

import br.com.sbrwgjd.controllers.*;
import br.com.sbrwgjd.data.dto.*;
import br.com.sbrwgjd.exception.*;
import br.com.sbrwgjd.mapper.*;
import br.com.sbrwgjd.model.*;
import br.com.sbrwgjd.repository.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class BookServices {

    Logger logger = LoggerFactory.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository bookRepository;

    public List<BooksDTO> findByAll(){

        logger.info("Finding all People!");

        var books = ObjectMapper.parseListObjects(bookRepository.findAll(), BooksDTO.class);
        books.forEach(this::addHateoasLinks);

        return books;
    }

    public BooksDTO findById(Long id){

        logger.info("Finding one Book!");

        var entity = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var dto = ObjectMapper.parseObject(entity, BooksDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BooksDTO create(BooksDTO bookDTO) {

        if(bookDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Book!");

        var entity = ObjectMapper.parseObject(bookDTO, Books.class);
        var dto = ObjectMapper.parseObject(bookRepository.save(entity), BooksDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BooksDTO update(BooksDTO bookDTO) {

        if(bookDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Book!");

        Books entity = bookRepository.findById(bookDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

        entity.setTitle(bookDTO.getTitle());
        entity.setAuthor((bookDTO.getAuthor()));
        entity.setPrice(bookDTO.getPrice());

        var dto = ObjectMapper.parseObject(bookRepository.save(entity), BooksDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {

        logger.info("Delete one Book!");

        Books entity = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this id.")
        );

        bookRepository.delete(entity);
    }

    private void addHateoasLinks(BooksDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("findAll").withType("DELETE"));
    }
}
