package com.example.libraryservice;


import com.example.libraryservice.controller.LibraryController;
import com.example.libraryservice.dto.BookStatusDto;
import com.example.libraryservice.model.BookStatus;
import com.example.libraryservice.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final static ModelMapper MODEL_MAPPER =new ModelMapper();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static ObjectMapper OBJECT_MAPPER =new ObjectMapper();
    private static ObjectWriter objectWriter;

    private static BookStatusDto bookStatusDto1;
    private static BookStatusDto bookStatusDto2;
    private static BookStatusDto bookStatusDto3;
    private static BookStatusDto updatingBookStatusDto;

    @MockBean
    private LibraryService libraryService;


    @BeforeAll
    static void setUp() {

        BookStatus bookStatus1=new BookStatus(1L, LocalDateTime.parse("2024-10-06 21:10:42", DATE_TIME_FORMATTER), LocalDateTime.parse("2024-12-06 21:10:42", DATE_TIME_FORMATTER));
        BookStatus bookStatus2=new BookStatus(2L,LocalDateTime.parse("2024-11-03 21:10:42", DATE_TIME_FORMATTER), LocalDateTime.parse("2024-12-06 21:10:42", DATE_TIME_FORMATTER));
        BookStatus bookStatus3=new BookStatus(3L,LocalDateTime.parse("2023-11-01 10:00:00", DATE_TIME_FORMATTER), LocalDateTime.parse("2024-12-06 21:10:42", DATE_TIME_FORMATTER));
        BookStatus updatingBookStatus=new BookStatus(3L,LocalDateTime.parse("2023-10-01 10:00:00", DATE_TIME_FORMATTER), LocalDateTime.parse("2024-12-23 21:10:42", DATE_TIME_FORMATTER));


        bookStatusDto1= MODEL_MAPPER.map(bookStatus1,BookStatusDto.class);
        bookStatusDto2= MODEL_MAPPER.map(bookStatus2,BookStatusDto.class);
        bookStatusDto3= MODEL_MAPPER.map(bookStatus3,BookStatusDto.class);
        updatingBookStatusDto= MODEL_MAPPER.map(updatingBookStatus,BookStatusDto.class);

        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        objectWriter = OBJECT_MAPPER.writer();

    }

    @Test
    void testFindBookStatus() throws Exception {

        when(libraryService.findAll()).thenReturn(List.of(bookStatusDto1,bookStatusDto2,bookStatusDto3));

        mockMvc.perform(get("/library/getStatuses"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$", hasSize(3)),
                        jsonPath("$[0].id",is(1)),
                        jsonPath("$[0].borrowedAt",is("2024-10-06 21:10:42")),
                        jsonPath("$[0].dueDate",is("2024-12-06 21:10:42")),
                        jsonPath("$[1].id",is(2)),
                        jsonPath("$[1].borrowedAt",is("2024-11-03 21:10:42")),
                        jsonPath("$[1].dueDate",is("2024-12-06 21:10:42")),
                        jsonPath("$[2].id",is(3)),
                        jsonPath("$[2].borrowedAt",is("2023-11-01 10:00:00")),
                        jsonPath("$[2].dueDate",is("2024-12-06 21:10:42"))
                        );
    }

    @Test
    void testFindBookStatusById() throws Exception {

        when(libraryService.findBookStatusById(bookStatusDto1.getId())).thenReturn(bookStatusDto1);

        mockMvc.perform(get("/library/getStatus/"+bookStatusDto1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpectAll(
                        jsonPath("$.id",is(1)),
                        jsonPath("$.borrowedAt",is("2024-10-06 21:10:42")),
                        jsonPath("$.dueDate",is("2024-12-06 21:10:42"))
                );
    }

    @Test
    void testUpdateBookStatus() throws Exception{

        when(libraryService.updateBookStatus(updatingBookStatusDto)).thenReturn(updatingBookStatusDto);

        String content = objectWriter.writeValueAsString(updatingBookStatusDto);

        MockHttpServletRequestBuilder mockMvcRequestBuilders = put("/library/updateStatus")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpectAll(
                        jsonPath("$.id",is(3)),
                        jsonPath("$.borrowedAt",is("2023-10-01 10:00:00")),
                        jsonPath("$.dueDate",is("2024-12-23 21:10:42"))
                );


    }

}
