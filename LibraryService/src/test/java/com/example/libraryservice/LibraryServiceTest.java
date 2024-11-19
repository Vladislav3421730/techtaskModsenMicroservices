package com.example.libraryservice;

import com.example.libraryservice.dto.BookStatusDto;
import com.example.libraryservice.exception.BookStatusNotFoundException;
import com.example.libraryservice.model.BookStatus;
import com.example.libraryservice.repository.BookStatusRepository;
import com.example.libraryservice.service.Impl.LibraryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class LibraryServiceTest {

    private final static ModelMapper MODEL_MAPPER =new ModelMapper();
    @Mock
    private BookStatusRepository bookStatusRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LibraryServiceImpl libraryService;

    private static BookStatus bookStatus;
    private static BookStatusDto bookStatusDto;

    @BeforeAll
    static void setUp() {

        bookStatus = BookStatus.builder()
                .id(1L)
                .borrowedAt(LocalDateTime.now().minusDays(3))
                .dueDate(LocalDateTime.now().plusDays(4))
                .build();

        bookStatusDto= MODEL_MAPPER.map(bookStatus, BookStatusDto.class);
    }

    @Test
    public void testFindAllBookStatuses() {
        when(bookStatusRepository.findAll()).thenReturn(List.of(bookStatus));
        when(modelMapper.map(bookStatus, BookStatusDto.class)).thenReturn(bookStatusDto);

        List<BookStatusDto> result = libraryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookStatusRepository).findAll();
    }

    @Test
    public void testFindBookStatusById() {
        when(bookStatusRepository.findById(1L)).thenReturn(Optional.of(bookStatus));
        when(modelMapper.map(bookStatus, BookStatusDto.class)).thenReturn(bookStatusDto);

        BookStatusDto result = libraryService.findBookStatusById(1L);

        assertNotNull(result);
        assertEquals(bookStatus.getBorrowedAt(), result.getBorrowedAt());
        verify(bookStatusRepository).findById(1L);
    }

    @Test
    public void testFindBookStatusById_NotFound() {

        when(bookStatusRepository.findById(1L)).thenReturn(Optional.empty());

        BookStatusNotFoundException thrown = assertThrows(BookStatusNotFoundException.class, () -> libraryService.findBookStatusById(1L));

        assertEquals("Status with id 1 wasn't found", thrown.getMessage());
    }

    @Test
    public void testUpdateBookStatus() {

        when(bookStatusRepository.findById(1L)).thenReturn(Optional.of(bookStatus));
        when(bookStatusRepository.save(bookStatus)).thenReturn(bookStatus);

        when(modelMapper.map(bookStatus, BookStatusDto.class)).thenReturn(bookStatusDto);

        BookStatusDto result = libraryService.updateBookStatus(bookStatusDto);

        assertNotNull(result);

        assertEquals(bookStatus.getBorrowedAt(), result.getBorrowedAt());

        assertEquals(bookStatus.getDueDate(), result.getDueDate());

        verify(bookStatusRepository).findById(1L);
        verify(bookStatusRepository).save(any(BookStatus.class));
    }

    @Test
    public void testUpdateBookStatus_NotFound() {

        when(bookStatusRepository.findById(1L)).thenReturn(Optional.empty());

        BookStatusNotFoundException thrown = assertThrows(BookStatusNotFoundException.class, () -> libraryService.updateBookStatus(bookStatusDto));

        assertEquals("Status with id 1 wasn't found. Updating is impossible", thrown.getMessage());
    }
}
