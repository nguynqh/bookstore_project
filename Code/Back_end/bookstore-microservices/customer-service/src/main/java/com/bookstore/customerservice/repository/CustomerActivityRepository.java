package com.bookstore.customerservice.repository;

import com.bookstore.customerservice.entity.CustomerActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerActivityRepository extends JpaRepository<CustomerActivity, UUID> {

    Page<CustomerActivity> findByCustomerId(UUID customerId, Pageable pageable);

    List<CustomerActivity> findTop10ByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    Page<CustomerActivity> findByCustomerIdAndActivityType(UUID customerId, String activityType, Pageable pageable);
}