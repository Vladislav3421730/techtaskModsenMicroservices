package com.example.bookservice.service.Impl;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.FreeBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;


@Service
@RequiredArgsConstructor
@Slf4j
public class FreeBookServiceImpl implements FreeBookService {

    private final static String GET_FREE_BOOK_STATUSES = "get-free-book-statuses";
    private final static String SEND_FREE_BOOK_STATUSES_ID = "send-free-book-statuses-id";

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private List<BookDto> freeBooks = new ArrayList<>();
    private CountDownLatch latch;

    @KafkaListener(topics = SEND_FREE_BOOK_STATUSES_ID, groupId = "book-service-group")
    public void listenerForFreeBookStatuses(String responseMessage) {

        log.info("Received free book statuses response: {}", responseMessage);

        processFreeBooksResponse(responseMessage);

    }

    @Override
    public List<BookDto> findFreeBooks()  {

        log.info("Sending data to topic: {}", GET_FREE_BOOK_STATUSES);

        kafkaTemplate.send(GET_FREE_BOOK_STATUSES, "request for free books");

        latch = new CountDownLatch(1);

        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Sending topic failed: {}",e.getMessage());
            throw new RuntimeException(e);
        }

        return freeBooks;
    }

    public void processFreeBooksResponse(String responseMessage) {
        log.info("Processing free books response: {}", responseMessage);

        if (!responseMessage.isEmpty()) {
            List<Long> freeBookIds = Arrays.stream(responseMessage.split(","))
                    .map(Long::parseLong)
                    .toList();

            log.info("Free book IDs: {}", freeBookIds);

            List<CompletableFuture<BookDto>> futures = freeBookIds.stream()
                    .map(id -> CompletableFuture.supplyAsync(() -> {
                        log.info("Fetching book with ID: {}", id);
                        return bookRepository.findById(id).map(book -> modelMapper.map(book, BookDto.class)).get();
                    }))
                    .toList();

            freeBooks = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            log.info("Processed {} free books asynchronously.", freeBooks.size());
        }
        latch.countDown();
    }
}
