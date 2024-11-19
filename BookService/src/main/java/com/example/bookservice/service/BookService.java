package com.example.bookservice.service;


import com.example.bookservice.dto.BookDto;

import java.util.List;

public interface BookService {

    BookDto save(BookDto bookDto);

    BookDto update(BookDto bookDto);

    void delete(Long id);

    List<BookDto> findAll();

    BookDto findByIsbn(String isbn);

    BookDto findById(Long id);


}
