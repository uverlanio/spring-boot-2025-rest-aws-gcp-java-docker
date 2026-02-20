package br.com.sbrwgjd.services;

import br.com.sbrwgjd.controllers.BookController;
import br.com.sbrwgjd.data.dto.BooksDTO;
import br.com.sbrwgjd.exception.RequiredObjectIsNullException;
import br.com.sbrwgjd.exception.ResourceNotFoundException;
import br.com.sbrwgjd.mapper.ObjectMapper;
import br.com.sbrwgjd.model.Books;
import br.com.sbrwgjd.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    Logger logger = LoggerFactory.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository bookRepository;
    @Autowired
    PagedResourcesAssembler<BooksDTO> assembler;

    public PagedModel<EntityModel<BooksDTO>> findByAll(Pageable pageable){

        logger.info("Finding all Books!");

        var books = bookRepository.findAll(pageable);

        var booksWithLinks = books.map(book -> {
            var dto = ObjectMapper.parseObject(book, BooksDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = linkTo(
                methodOn(BookController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                String.valueOf(pageable.getSort())
                        )).withSelfRel();

        return assembler.toModel(booksWithLinks, findAllLink);
    }

    public PagedModel<EntityModel<BooksDTO>> findByTitle(String title, Pageable pageable){

        logger.info("Finding Books by title!");

        var books = bookRepository.findBookByTitle(title, pageable);

        var booksWithLinks = books.map(book -> {
            var dto = ObjectMapper.parseObject(book, BooksDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                String.valueOf(pageable.getSort())
                        )).withSelfRel();

        return assembler.toModel(booksWithLinks, findAllLink);
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
        dto.add(linkTo(methodOn(BookController.class).findAll(1,12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("findAll").withType("DELETE"));
    }
}
