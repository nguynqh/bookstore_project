package com.bookstore.bookservice.repository;

import com.bookstore.bookservice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    Page<Book> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE %:keyword% OR " +
            "LOWER(b.author) LIKE %:keyword% OR " +
            "LOWER(b.description) LIKE %:keyword%")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.name = :categoryName")
    List<Book> findByCategoryName(@Param("categoryName") String categoryName);
}