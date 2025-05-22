package com.bookstore.searchservice.event;

import com.bookstore.searchservice.document.BookDocument;
import com.bookstore.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookEventListener {

    private final SearchService searchService;
    private final ModelMapper modelMapper;

    @KafkaListener(topics = "book-events", groupId = "${spring.kafka.consumer.group-id}")
    public void processBookEvent(BookEvent event) {
        log.info("Received book event: {}", event.getEventType());

        switch (event.getEventType()) {
            case "CREATED":
            case "UPDATED":
                BookDocument bookDocument = convertToDocument(event.getBook());
                searchService.saveBook(bookDocument);
                log.info("Book indexed: {}", bookDocument.getId());
                break;

            case "DELETED":
                searchService.deleteBook(event.getBook().getId());
                log.info("Book deleted from index: {}", event.getBook().getId());
                break;

            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }

    private BookDocument convertToDocument(BookEvent.BookEventData bookData) {
        BookDocument bookDocument = modelMapper.map(bookData, BookDocument.class);

        if (bookData.getCategories() != null) {
            bookDocument.setCategories(
                    bookData.getCategories().stream()
                            .map(category -> modelMapper.map(category, BookDocument.Category.class))
                            .collect(Collectors.toSet())
            );
        }

        return bookDocument;
    }
}