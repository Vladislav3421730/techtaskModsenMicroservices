package com.example.libraryservice.service.Impl;

import com.example.libraryservice.dto.BookStatusDto;
import com.example.libraryservice.exception.BookStatusNotFoundException;
import com.example.libraryservice.model.BookStatus;
import com.example.libraryservice.repository.BookStatusRepository;
import com.example.libraryservice.service.LibraryService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class LibraryServiceImpl implements LibraryService {

    BookStatusRepository bookStatusRepository;
    ModelMapper modelMapper;

    @Override
    public void saveBookStatus(Long id) {

        log.info("Attempting to save book status with ID: {}", id);

        BookStatus bookStatus = BookStatus.builder()
                .id(id)
                .build();

        bookStatusRepository.save(bookStatus);
        log.info("Successfully saved book status with ID: {}", id);
    }

    @Override
    public String findFreeBookStatusesId() {

        return   bookStatusRepository.findAll().stream()
                .filter(bookStatus -> bookStatus.getDueDate().isBefore(LocalDateTime.now()))
                .toList()
                .stream()
                .map(bookStatus -> bookStatus.getId().toString())
                .collect(Collectors.joining(","));

    }

    @Override
    public List<BookStatusDto> findAll() {

        log.info("Fetching all book statuses from the repository");

        List<BookStatusDto> bookStatusDtoList = bookStatusRepository.findAll().stream()
                .map(bookStatus -> modelMapper.map(bookStatus, BookStatusDto.class))
                .collect(Collectors.toList());

        log.info("Successfully fetched {} book statuses", bookStatusDtoList.size());
        return bookStatusDtoList;
    }

    @Override
    public BookStatusDto findBookStatusById(Long id) {

        log.info("Attempting to find book status with ID: {}", id);

        BookStatus bookStatus = bookStatusRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book status with ID {} not found", id);
                    return new BookStatusNotFoundException(String.format("Status with id %d wasn't found", id));
                });

        log.info("Successfully found book status with ID: {}", id);
        return modelMapper.map(bookStatus, BookStatusDto.class);
    }

    @Override
    @Transactional
    public BookStatusDto updateBookStatus(BookStatusDto bookStatusDto) {

        log.info("Attempting to update book status with ID: {}", bookStatusDto.getId());

        BookStatus existingBookStatus = bookStatusRepository.findById(bookStatusDto.getId())
                .orElseThrow(() -> {
                    log.error("Book status with ID {} not found. Cannot update", bookStatusDto.getId());
                    return new BookStatusNotFoundException(
                            String.format("Status with id %d wasn't found. Updating is impossible", bookStatusDto.getId()));
                });

        log.info("Found existing book status with ID: {}. Updating with new details", bookStatusDto.getId());

        modelMapper.map(bookStatusDto, existingBookStatus);
        BookStatus updatedBookStatus = bookStatusRepository.save(existingBookStatus);

        log.info("Successfully updated book status with ID: {}", updatedBookStatus.getId());
        return modelMapper.map(updatedBookStatus, BookStatusDto.class);
    }

}
