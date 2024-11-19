package com.example.libraryservice.service;

import com.example.libraryservice.dto.BookStatusDto;

import java.util.List;

public interface LibraryService {

    void saveBookStatus(Long id);

    String findFreeBookStatusesId();

    List<BookStatusDto> findAll();
    BookStatusDto findBookStatusById(Long id);
    BookStatusDto updateBookStatus(BookStatusDto bookStatusDto);
}
