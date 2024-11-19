package com.example.bookservice.service.Impl;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookServiceImpl implements BookService {

    private final static String TOPIC_SEND_BOOK_ID = "send-book-id";

    KafkaTemplate<String, String> kafkaTemplate;
    BookRepository bookRepository;
    ModelMapper modelMapper;

    @Override
    @Transactional
    public BookDto save(BookDto bookDto) {

        log.info("Attempting to save a new book: {}", bookDto.getName());

        Book book = modelMapper.map(bookDto, Book.class);
        bookDto = modelMapper.map(bookRepository.save(book), BookDto.class);

        kafkaTemplate.send(TOPIC_SEND_BOOK_ID, String.valueOf(book.getId()));

        log.info("Book saved successfully with ID: {}", bookDto.getId());

        return bookDto;
    }

    @Override
    @Transactional
    public BookDto update(BookDto bookDto) {

        log.info("Attempting to update book with ID: {}", bookDto.getId());

        Book existingBook = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found. Updating is impossible.", bookDto.getId());
                    return new BookNotFoundException(String.format("Book with id %d wasn't found. Updating is impossible", bookDto.getId()));
                });

        modelMapper.map(bookDto, existingBook);
        BookDto updatedBookDto = modelMapper.map(bookRepository.save(existingBook), BookDto.class);

        log.info("Book with ID {} updated successfully.", updatedBookDto.getId());
        return updatedBookDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {

        log.info("Attempting to delete book with ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found. Deleting is impossible.", id);
                    return new BookNotFoundException(String.format("Book with id %d wasn't found. Deleting is impossible", id));
                });

        bookRepository.delete(book);
        log.info("Book with ID {} deleted successfully.", id);
    }

    @Override
    public List<BookDto> findAll() {

        log.info("Retrieving all books");

        List<BookDto> books = bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());

        log.info("Retrieved {} books successfully.", books.size());
        return books;
    }

    @Override
    public BookDto findByIsbn(String isbn) {

        log.info("Attempting to find book by ISBN: {}", isbn);

        Book book = bookRepository.findByISBN(isbn)
                .orElseThrow(() -> {
                    log.error("Book with ISBN {} not found", isbn);
                    return new BookNotFoundException(String.format("Book with ISBN %s wasn't found", isbn));
                });

        log.info("Book with ISBN {} found successfully.", isbn);
        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public BookDto findById(Long id) {

        log.info("Attempting to find book by ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found", id);
                    return new BookNotFoundException(String.format("Book with id %d wasn't found", id));
                });

        log.info("Book with ID {} found successfully.", id);
        return modelMapper.map(book, BookDto.class);
    }



}
