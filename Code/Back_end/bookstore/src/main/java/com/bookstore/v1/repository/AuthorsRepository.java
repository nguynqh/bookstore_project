package com.bookstore.v1.repository;

import com.bookstore.v1.entity.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Long> {
    Authors findByName(String name);
}
