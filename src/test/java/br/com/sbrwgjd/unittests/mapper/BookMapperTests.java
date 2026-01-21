package br.com.sbrwgjd.unittests.mapper;

import br.com.sbrwgjd.data.dto.*;
import br.com.sbrwgjd.model.*;
import br.com.sbrwgjd.unittests.mapper.mocks.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static br.com.sbrwgjd.mapper.ObjectMapper.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookMapperTests {
    MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    public void parseEntityToDTOTest() {
        BookDTO output = parseObject(inputObject.mockEntity(), BookDTO.class);
        assertEquals(0L, output.getId());
        assertEquals("O Hobbit0", output.getTitle());
        assertEquals("J.R.R. Tolkien0", output.getAuthor());
        assertEquals(125.50, output.getPrice());
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<BookDTO> outputList = parseListObjects(inputObject.mockEntityList(), BookDTO.class);
        BookDTO outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("O Hobbit0", outputZero.getTitle());
        assertEquals("J.R.R. Tolkien0", outputZero.getAuthor());
        assertEquals(125.50, outputZero.getPrice());

        BookDTO outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("O Hobbit7", outputSeven.getTitle());
        assertEquals("J.R.R. Tolkien7", outputSeven.getAuthor());
        assertEquals(125.50, outputSeven.getPrice());

        BookDTO outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("O Hobbit12", outputTwelve.getTitle());
        assertEquals("J.R.R. Tolkien12", outputTwelve.getAuthor());
        assertEquals(125.50, outputTwelve.getPrice());
    }

    @Test
    public void parseDTOToEntityTest() {
        Books output = parseObject(inputObject.mockDTO(), Books.class);
        assertEquals(0L, output.getId());
        assertEquals("O Hobbit0", output.getTitle());
        assertEquals("J.R.R. Tolkien0", output.getAuthor());
        assertEquals(125.50, output.getPrice());
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Books> outputList = parseListObjects(inputObject.mockDTOList(), Books.class);
        Books outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("O Hobbit0", outputZero.getTitle());
        assertEquals("J.R.R. Tolkien0", outputZero.getAuthor());
        assertEquals(125.50, outputZero.getPrice());

        Books outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("O Hobbit7", outputSeven.getTitle());
        assertEquals("J.R.R. Tolkien7", outputSeven.getAuthor());
        assertEquals(125.50, outputSeven.getPrice());

        Books outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("O Hobbit12", outputTwelve.getTitle());
        assertEquals("J.R.R. Tolkien12", outputTwelve.getAuthor());
        assertEquals(125.50, outputTwelve.getPrice());
    }
}