package com.bookstore.v1.repository;

import com.bookstore.v1.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
    Books findByTitle(String title);
}
