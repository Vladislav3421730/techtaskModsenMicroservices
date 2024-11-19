package com.example.bookservice;

import com.example.bookservice.controller.BookController;
import com.example.bookservice.dto.BookDto;
import com.example.bookservice.model.Book;
import com.example.bookservice.service.BookService;
import com.example.bookservice.service.FreeBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final static ModelMapper MODEL_MAPPER = new ModelMapper();
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final static ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    private static BookDto bookDto1;
    private static BookDto bookDto2;
    private static BookDto bookDto3;

    @MockBean
    private BookService bookService;

    @MockBean
    private FreeBookService freeBookService;

    @BeforeAll
    static void setUp() {

        Book book1 = new Book(1L, "12789812-11", "The Great Adventure", "Adventure", "An epic journey.", "John Doe");
        Book book2 = new Book(2L, "12789812-12", "Mystery of the Lake", "Mystery", "Secrets beneath the surface.", "Jane Smith");
        Book book3 = new Book(3L, "12789812-13", "Science Explained", "Education", "A dive into scientific concepts.", "Dr. Science");

        bookDto1 = MODEL_MAPPER.map(book1, BookDto.class);
        bookDto2 = MODEL_MAPPER.map(book2, BookDto.class);
        bookDto3 = MODEL_MAPPER.map(book3, BookDto.class);
    }

    @Test
    @DisplayName("Test retrieving all books")
    void testFindAllBooks() throws Exception {

        when(bookService.findAll()).thenReturn(List.of(bookDto1, bookDto2, bookDto3));

        mockMvc.perform(get("/book/get"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$", hasSize(3)),
                        jsonPath("$[0].id", is(1)),
                        jsonPath("$[0].isbn", is("12789812-11")),
                        jsonPath("$[0].name", is("The Great Adventure")),
                        jsonPath("$[0].genre", is("Adventure")),
                        jsonPath("$[0].description", is("An epic journey.")),
                        jsonPath("$[0].author", is("John Doe")),
                        jsonPath("$[1].id", is(2)),
                        jsonPath("$[1].isbn", is("12789812-12")),
                        jsonPath("$[1].name", is("Mystery of the Lake")),
                        jsonPath("$[1].genre", is("Mystery")),
                        jsonPath("$[1].description", is("Secrets beneath the surface.")),
                        jsonPath("$[1].author", is("Jane Smith")),
                        jsonPath("$[2].id", is(3)),
                        jsonPath("$[2].isbn", is("12789812-13")),
                        jsonPath("$[2].name", is("Science Explained")),
                        jsonPath("$[2].genre", is("Education")),
                        jsonPath("$[2].description", is("A dive into scientific concepts.")),
                        jsonPath("$[2].author", is("Dr. Science"))
                );
        verify(bookService, times(1)).findAll();
    }

    @Test
    @DisplayName("Test retrieving free books")
    void testFindFreeBooks() throws Exception {

        when(freeBookService.findFreeBooks()).thenReturn(List.of(bookDto1, bookDto2));

        mockMvc.perform(get("/book/free"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].id", is(1)),
                        jsonPath("$[0].isbn", is("12789812-11")),
                        jsonPath("$[0].name", is("The Great Adventure")),
                        jsonPath("$[0].genre", is("Adventure")),
                        jsonPath("$[0].description", is("An epic journey.")),
                        jsonPath("$[0].author", is("John Doe")),
                        jsonPath("$[1].id", is(2)),
                        jsonPath("$[1].isbn", is("12789812-12")),
                        jsonPath("$[1].name", is("Mystery of the Lake")),
                        jsonPath("$[1].genre", is("Mystery")),
                        jsonPath("$[1].description", is("Secrets beneath the surface.")),
                        jsonPath("$[1].author", is("Jane Smith"))
                );
        verify(freeBookService, times(1)).findFreeBooks();
    }

    @Test
    @DisplayName("Test saving a new book")
    void testSaveBook() throws Exception {

        Book book = new Book(4L, "12789812-14", "New Discoveries", "Science", "Exploring the unknown.", "Alice Johnson");
        BookDto addingBook = MODEL_MAPPER.map(book, BookDto.class);

        when(bookService.save(addingBook)).thenReturn(addingBook);
        String content = OBJECT_WRITER.writeValueAsString(addingBook);

        MockHttpServletRequestBuilder mockMvcRequestBuilders = post("/book/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpectAll(
                        jsonPath("$.id", is(4)),
                        jsonPath("$.isbn", is("12789812-14")),
                        jsonPath("$.name", is("New Discoveries")),
                        jsonPath("$.genre", is("Science")),
                        jsonPath("$.description", is("Exploring the unknown.")),
                        jsonPath("$.author", is("Alice Johnson"))
                );
        verify(bookService, times(1)).save(addingBook);
    }

    @Test
    @DisplayName("Test finding a book by its ID")
    void testFindBookById() throws Exception {

        when(bookService.findById(bookDto1.getId())).thenReturn(bookDto1);

        mockMvc.perform(get("/book/get/" + bookDto1.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id", is(1)),
                        jsonPath("$.isbn", is("12789812-11")),
                        jsonPath("$.name", is("The Great Adventure")),
                        jsonPath("$.genre", is("Adventure")),
                        jsonPath("$.description", is("An epic journey.")),
                        jsonPath("$.author", is("John Doe"))
                );

        verify(bookService, times(1)).findById(bookDto1.getId());
    }

    @Test
    @DisplayName("Test finding a book by its ISBN")
    void testFindBookByISBN() throws Exception {

        when(bookService.findByIsbn(bookDto2.getIsbn())).thenReturn(bookDto2);

        mockMvc.perform(get("/book/get/isbn/" + bookDto2.getIsbn()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id", is(2)),
                        jsonPath("$.isbn", is("12789812-12")),
                        jsonPath("$.name", is("Mystery of the Lake")),
                        jsonPath("$.genre", is("Mystery")),
                        jsonPath("$.description", is("Secrets beneath the surface.")),
                        jsonPath("$.author", is("Jane Smith"))
                );

        verify(bookService, times(1)).findByIsbn(bookDto2.getIsbn());
    }

    @Test
    @DisplayName("Test updating an existing book")
    public void testUpdateBook() throws Exception {

        Book book = new Book(1L, "12789812-15", "The Updated Adventure", "New Genre", "A revised journey.", "John Doe");
        BookDto updatingBook = MODEL_MAPPER.map(book, BookDto.class);

        when(bookService.update(updatingBook)).thenReturn(updatingBook);

        String content = OBJECT_WRITER.writeValueAsString(updatingBook);

        MockHttpServletRequestBuilder mockMvcRequestBuilders = put("/book/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpectAll(
                        jsonPath("$.id", is(1)),
                        jsonPath("$.isbn", is("12789812-15")),
                        jsonPath("$.name", is("The Updated Adventure")),
                        jsonPath("$.genre", is("New Genre")),
                        jsonPath("$.description", is("A revised journey.")),
                        jsonPath("$.author", is("John Doe"))
                );
        verify(bookService, times(1)).update(updatingBook);
    }

    @Test
    @DisplayName("Test deleting a book by its ID")
    public void testDeleteBook() throws Exception {

        mockMvc.perform(delete("/book/delete/" + bookDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message", is("Book with id 1 has been deleted")));

        verify(bookService, times(1)).delete(bookDto1.getId());
    }
}
