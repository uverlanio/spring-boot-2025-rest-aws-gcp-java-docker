package br.com.sbrwgjd.controllers;

import br.com.sbrwgjd.controllers.docs.*;
import br.com.sbrwgjd.data.dto.*;
import br.com.sbrwgjd.services.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Tag(name = "Book", description = "Endpoints for Managing Books")
@RequestMapping("/api/book/v1")
public class BookController implements BookControllerDocs {

    @Autowired
    private BookServices bookServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
   // @Override
    public List<BooksDTO> findAll(){

        return bookServices.findByAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
   // @Override
    public BooksDTO findById(@PathVariable("id") Long id){

        return bookServices.findById(id);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
   // @Override
    public BooksDTO create(@RequestBody BooksDTO book){
        return bookServices.create(book);
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
   // @Override
    public BooksDTO update(@RequestBody BooksDTO book){

        var b = bookServices.findById(book.getId());

        if(Objects.nonNull(b)){
            return bookServices.update(book);
        }
        return new BooksDTO();
    }

    @DeleteMapping("/{id}")
   // @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){

        var b = bookServices.findById(id);

        if(Objects.nonNull(b)){
            bookServices.delete(b.getId());
        }

        return ResponseEntity.noContent().build();
    }
}
