package com.example.libraryservice.config;

import com.example.libraryservice.service.LibraryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaListenerConfig {

    private final static String SEND_BOOK_ID = "send-book-id";
    private final static String GET_FREE_BOOK_STATUSES = "get-free-book-statuses";
    private final static String SEND_FREE_BOOK_STATUSES_ID = "send-free-book-statuses-id";

    LibraryService libraryService;
    KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = SEND_BOOK_ID, groupId = "my-consumer-group")
    void listenerForAddingBookStatus(String data) {

        try {

            log.info("Received message to add book status. Book ID: {}", data);

            Long bookId = Long.parseLong(data);

            log.info("Saving status for book with ID: {}", bookId);
            libraryService.saveBookStatus(bookId);

            log.info("Successfully saved book status for book with ID: {}", bookId);
        } catch (NumberFormatException e) {
            log.error("Invalid book ID received: {}. Unable to process the message.", data);
        }
    }

    @KafkaListener(topics = GET_FREE_BOOK_STATUSES, groupId = "my-consumer-group")
    void listenerForFindingFreeBooks(String data) {

        try {

            log.info("Received request to fetch free book statuses. Request data: {}", data);

            log.info("Fetching free book statuses...");
            String freeBookStatuses = libraryService.findFreeBookStatusesId();
            log.info("Successfully fetched {} free book statuses. Sending response...", freeBookStatuses.split(",").length);

            kafkaTemplate.send(SEND_FREE_BOOK_STATUSES_ID, freeBookStatuses);
            log.info("Successfully sent free book statuses {} response to Kafka topic: {}",freeBookStatuses, SEND_FREE_BOOK_STATUSES_ID);

        } catch (Exception e) {
            log.error("Error occurred while fetching free book statuses. Error: {}", e.getMessage(), e);
        }
    }
}
