package com.bookstore.bookservice.service.impl;

import com.bookstore.bookservice.dto.BookDTO;
import com.bookstore.bookservice.dto.BookRequest;
import com.bookstore.bookservice.dto.PagedResponse;
import com.bookstore.bookservice.entity.Book;
import com.bookstore.bookservice.entity.Category;
import com.bookstore.bookservice.event.BookEvent;
import com.bookstore.bookservice.exception.BookNotFoundException;
import com.bookstore.bookservice.exception.CategoryNotFoundException;
import com.bookstore.bookservice.exception.ResourceAlreadyExistsException;
import com.bookstore.bookservice.repository.BookRepository;
import com.bookstore.bookservice.repository.CategoryRepository;
import com.bookstore.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.book-events}")
    private String bookEventsTopic;

    @Override
    public PagedResponse<BookDTO> getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return createPagedResponse(bookPage);
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookDTO getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    @Cacheable(value = "books", key = "#isbn")
    public BookDTO getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with isbn: " + isbn));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    @Transactional
    public BookDTO createBook(BookRequest bookRequest) {
        // Check if book with same ISBN already exists
        if (bookRequest.getIsbn() != null && !bookRequest.getIsbn().isEmpty()) {
            Optional<Book> existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
            if (existingBook.isPresent()) {
                throw new ResourceAlreadyExistsException("Book with ISBN " + bookRequest.getIsbn() + " already exists");
            }
        }

        Book book = modelMapper.map(bookRequest, Book.class);

        // Set categories
        if (bookRequest.getCategoryIds() != null && !bookRequest.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (UUID categoryId : bookRequest.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
                categories.add(category);
            }
            book.setCategories(categories);
        }

        Book savedBook = bookRepository.save(book);
        BookDTO bookDTO = modelMapper.map(savedBook, BookDTO.class);

        // Publish book created event
        publishBookEvent("CREATED", bookDTO);

        return bookDTO;
    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public BookDTO updateBook(UUID id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        // Check ISBN uniqueness if changed
        if (bookRequest.getIsbn() != null && !bookRequest.getIsbn().equals(book.getIsbn())) {
            Optional<Book> bookWithSameIsbn = bookRepository.findByIsbn(bookRequest.getIsbn());
            if (bookWithSameIsbn.isPresent() && !bookWithSameIsbn.get().getId().equals(id)) {
                throw new ResourceAlreadyExistsException("Book with ISBN " + bookRequest.getIsbn() + " already exists");
            }
        }

        // Update book properties
        if (bookRequest.getTitle() != null) book.setTitle(bookRequest.getTitle());
        if (bookRequest.getIsbn() != null) book.setIsbn(bookRequest.getIsbn());
        if (bookRequest.getAuthor() != null) book.setAuthor(bookRequest.getAuthor());
        if (bookRequest.getPublisher() != null) book.setPublisher(bookRequest.getPublisher());
        if (bookRequest.getSellingPrice() != null) book.setSellingPrice(bookRequest.getSellingPrice());
        if (bookRequest.getCostPrice() != null) book.setCostPrice(bookRequest.getCostPrice());
        if (bookRequest.getPublicationDate() != null) book.setPublicationDate(bookRequest.getPublicationDate());
        if (bookRequest.getDescription() != null) book.setDescription(bookRequest.getDescription());
        if (bookRequest.getImageUrl() != null) book.setImageUrl(bookRequest.getImageUrl());
        if (bookRequest.getPages() != null) book.setPages(bookRequest.getPages());

        // Update categories if provided
        if (bookRequest.getCategoryIds() != null) {
            Set<Category> newCategories = new HashSet<>();
            for (UUID categoryId : bookRequest.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
                newCategories.add(category);
            }
            book.getCategories().clear();
            book.setCategories(newCategories);
        }

        Book updatedBook = bookRepository.save(book);
        BookDTO bookDTO = modelMapper.map(updatedBook, BookDTO.class);

        // Publish book updated event
        publishBookEvent("UPDATED", bookDTO);

        return bookDTO;
    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);

        bookRepository.delete(book);

        // Publish book deleted event
        publishBookEvent("DELETED", bookDTO);
    }

    @Override
    public List<BookDTO> getBooksByCategory(UUID categoryId) {
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }

        List<Book> books = bookRepository.findByCategoryId(categoryId);
        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PagedResponse<BookDTO> searchBooks(String keyword, Pageable pageable) {
        Page<Book> bookPage = bookRepository.searchBooks(keyword.toLowerCase(), pageable);
        return createPagedResponse(bookPage);
    }

    private PagedResponse<BookDTO> createPagedResponse(Page<Book> bookPage) {
        List<BookDTO> bookDTOs = bookPage.getContent().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());

        return PagedResponse.<BookDTO>builder()
                .content(bookDTOs)
                .page(bookPage.getNumber())
                .size(bookPage.getSize())
                .totalElements(bookPage.getTotalElements())
                .totalPages(bookPage.getTotalPages())
                .last(bookPage.isLast())
                .build();
    }

    private void publishBookEvent(String eventType, BookDTO bookDTO) {
        BookEvent bookEvent = BookEvent.builder()
                .eventType(eventType)
                .book(bookDTO)
                .timestamp(LocalDateTime.now())
                .build();

        try {
            kafkaTemplate.send(bookEventsTopic, bookDTO.getId().toString(), bookEvent);
            log.info("Book event published: {}", eventType);
        } catch (Exception e) {
            log.error("Failed to publish book event: {}", e.getMessage());
        }
    }
}