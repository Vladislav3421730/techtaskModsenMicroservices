package com.example.bookservice.service;

import com.example.bookservice.dto.BookDto;

import java.util.List;

public interface FreeBookService {

    List<BookDto> findFreeBooks();


}
