package com.bookstore.customerservice.controller;

import com.bookstore.customerservice.dto.CustomerActivityDTO;
import com.bookstore.customerservice.dto.CustomerActivityRequest;
import com.bookstore.customerservice.service.CustomerActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer-activities")
@Slf4j
public class CustomerActivityController {

    private final CustomerActivityService activityService;

    @Autowired
    public CustomerActivityController(CustomerActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<CustomerActivityDTO>> getCustomerActivities(
            @PathVariable UUID customerId,
            Pageable pageable) {
        log.info("GET request to fetch activities for customer ID: {}", customerId);
        return ResponseEntity.ok(activityService.getCustomerActivities(customerId, pageable));
    }

    @GetMapping("/customer/{customerId}/recent")
    public ResponseEntity<List<CustomerActivityDTO>> getRecentActivities(@PathVariable UUID customerId) {
        log.info("GET request to fetch recent activities for customer ID: {}", customerId);
        return ResponseEntity.ok(activityService.getRecentActivities(customerId));
    }

    @GetMapping("/customer/{customerId}/type/{activityType}")
    public ResponseEntity<Page<CustomerActivityDTO>> getActivitiesByType(
            @PathVariable UUID customerId,
            @PathVariable String activityType,
            Pageable pageable) {
        log.info("GET request to fetch activities for customer ID: {} with type: {}", customerId, activityType);
        return ResponseEntity.ok(activityService.getCustomerActivitiesByType(customerId, activityType, pageable));
    }

    @PostMapping
    public ResponseEntity<CustomerActivityDTO> recordActivity(@Valid @RequestBody CustomerActivityRequest request) {
        log.info("POST request to record activity for customer ID: {}, type: {}",
                request.getCustomerId(), request.getActivityType());
        CustomerActivityDTO activity = activityService.recordActivity(request);
        return new ResponseEntity<>(activity, HttpStatus.CREATED);
    }
}