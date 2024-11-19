package com.example.bookservice;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;


    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setISBN("123-456-789");

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName("Test Book");
        bookDto.setIsbn("123-456-789");
    }

    @Test
    @DisplayName("Test saving a new book")
    public void testSaveBook() {

        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);

        BookDto result = bookService.save(bookDto);

        assertNotNull(result);
        assertEquals("Test Book", result.getName());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Test updating an existing book")
    public void testUpdateBook() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        BookDto result = bookService.update(bookDto);

        assertNotNull(result);
        assertEquals("Test Book", result.getName());
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Test updating a book that does not exist")
    public void testUpdateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> bookService.update(bookDto));
        assertEquals("Book with id 1 wasn't found. Updating is impossible", thrown.getMessage());
    }

    @Test
    @DisplayName("Test deleting an existing book")
    public void testDeleteBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.delete(1L);

        verify(bookRepository).delete(any(Book.class));
    }

    @Test
    @DisplayName("Test deleting a book that does not exist")
    public void testDeleteBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> bookService.delete(1L));
        assertEquals("Book with id 1 wasn't found. Deleting is impossible", thrown.getMessage());
    }

    @Test
    @DisplayName("Test retrieving all books")
    public void testFindAllBooks() {

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Another Book");
        when(bookRepository.findAll()).thenReturn(List.of(book, book2));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        List<BookDto> result = bookService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("Test finding a book by its ISBN")
    public void testFindByIsbn() {
        when(bookRepository.findByISBN("123-456-789")).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        BookDto result = bookService.findByIsbn("123-456-789");

        assertNotNull(result);
        assertEquals("Test Book", result.getName());
        verify(bookRepository).findByISBN("123-456-789");
    }

    @Test
    @DisplayName("Test finding a book by ISBN when not found")
    public void testFindByIsbn_NotFound() {
        when(bookRepository.findByISBN("123-456-789")).thenReturn(Optional.empty());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> bookService.findByIsbn("123-456-789"));
        assertEquals("Book with ISBN 123-456-789 wasn't found", thrown.getMessage());
    }

    @Test
    @DisplayName("Test finding a book by its ID")
    public void testFindById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        BookDto result = bookService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getName());
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("Test finding a book by ID when not found")
    public void testFindById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> bookService.findById(1L));
        assertEquals("Book with id 1 wasn't found", thrown.getMessage());
    }
}
