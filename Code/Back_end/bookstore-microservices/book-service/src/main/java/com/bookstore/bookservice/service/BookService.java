package com.bookstore.bookservice.service;

import com.bookstore.bookservice.dto.BookDTO;
import com.bookstore.bookservice.dto.BookRequest;
import com.bookstore.bookservice.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BookService {

    PagedResponse<BookDTO> getAllBooks(Pageable pageable);

    BookDTO getBookById(UUID id);

    BookDTO getBookByIsbn(String isbn);

    BookDTO createBook(BookRequest bookRequest);

    BookDTO updateBook(UUID id, BookRequest bookRequest);

    void deleteBook(UUID id);

    List<BookDTO> getBooksByCategory(UUID categoryId);

    PagedResponse<BookDTO> searchBooks(String keyword, Pageable pageable);
}