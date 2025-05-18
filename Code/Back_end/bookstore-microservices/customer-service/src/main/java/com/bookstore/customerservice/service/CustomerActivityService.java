package com.bookstore.customerservice.service;

import com.bookstore.customerservice.dto.CustomerActivityDTO;
import com.bookstore.customerservice.dto.CustomerActivityRequest;
import com.bookstore.customerservice.entity.Customer;
import com.bookstore.customerservice.entity.CustomerActivity;
import com.bookstore.customerservice.exception.CustomerNotFoundException;
import com.bookstore.customerservice.repository.CustomerActivityRepository;
import com.bookstore.customerservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerActivityService {

    private final CustomerActivityRepository activityRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerActivityService(CustomerActivityRepository activityRepository,
                                   CustomerRepository customerRepository,
                                   ModelMapper modelMapper) {
        this.activityRepository = activityRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public Page<CustomerActivityDTO> getCustomerActivities(UUID customerId, Pageable pageable) {
        log.debug("Fetching activities for customer: {}", customerId);

        // Verify customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }

        return activityRepository.findByCustomerId(customerId, pageable)
                .map(activity -> modelMapper.map(activity, CustomerActivityDTO.class));
    }

    public List<CustomerActivityDTO> getRecentActivities(UUID customerId) {
        log.debug("Fetching recent activities for customer: {}", customerId);

        // Verify customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }

        return activityRepository.findTop10ByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(activity -> modelMapper.map(activity, CustomerActivityDTO.class))
                .collect(Collectors.toList());
    }

    public Page<CustomerActivityDTO> getCustomerActivitiesByType(UUID customerId, String activityType, Pageable pageable) {
        log.debug("Fetching activities for customer: {} with type: {}", customerId, activityType);

        // Verify customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }

        return activityRepository.findByCustomerIdAndActivityType(customerId, activityType, pageable)
                .map(activity -> modelMapper.map(activity, CustomerActivityDTO.class));
    }

    @Transactional
    public CustomerActivityDTO recordActivity(CustomerActivityRequest request) {
        log.debug("Recording activity: {}", request);

        UUID customerId = request.getCustomerId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

        CustomerActivity activity = new CustomerActivity();
        activity.setId(UUID.randomUUID());
        activity.setCustomer(customer);
        activity.setActivityType(request.getActivityType());
        activity.setActivityData(request.getActivityData());
        activity.setCreatedAt(LocalDateTime.now());

        CustomerActivity savedActivity = activityRepository.save(activity);
        return modelMapper.map(savedActivity, CustomerActivityDTO.class);
    }

    @Transactional
    public CustomerActivityDTO recordActivity(UUID customerId, String activityType, String activityData) {
        log.debug("Recording activity for customer: {}, type: {}", customerId, activityType);

        CustomerActivityRequest request = new CustomerActivityRequest();
        request.setCustomerId(customerId);
        request.setActivityType(activityType);
        request.setActivityData(activityData);

        return recordActivity(request);
    }
}