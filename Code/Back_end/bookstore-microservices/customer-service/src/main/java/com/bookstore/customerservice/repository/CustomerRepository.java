package com.bookstore.customerservice.repository;

import com.bookstore.customerservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR c.phone LIKE CONCAT('%', :searchTerm, '%')")
    Page<Customer> findBySearchTerm(String searchTerm, Pageable pageable);

    Page<Customer> findByCustomerType(String customerType, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}