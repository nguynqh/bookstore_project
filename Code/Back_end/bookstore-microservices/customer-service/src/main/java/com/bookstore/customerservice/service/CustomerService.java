package com.bookstore.customerservice.service;

import com.bookstore.customerservice.dto.CustomerDTO;
import com.bookstore.customerservice.dto.CustomerRequest;
import com.bookstore.customerservice.entity.Customer;
import com.bookstore.customerservice.exception.CustomerAlreadyExistsException;
import com.bookstore.customerservice.exception.CustomerNotFoundException;
import com.bookstore.customerservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final CustomerActivityService customerActivityService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           ModelMapper modelMapper,
                           CustomerActivityService customerActivityService) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.customerActivityService = customerActivityService;
    }

    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        log.debug("Fetching all customers with pagination");
        return customerRepository.findAll(pageable)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    public CustomerDTO getCustomerById(UUID id) {
        log.debug("Fetching customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Page<CustomerDTO> searchCustomers(String searchTerm, Pageable pageable) {
        log.debug("Searching customers with term: {}", searchTerm);
        return customerRepository.findBySearchTerm(searchTerm, pageable)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    public Page<CustomerDTO> getCustomersByType(String customerType, Pageable pageable) {
        log.debug("Fetching customers by type: {}", customerType);
        return customerRepository.findByCustomerType(customerType, pageable)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerRequest customerRequest) {
        log.debug("Creating new customer: {}", customerRequest);

        // Check if customer with same email already exists
        if (customerRequest.getEmail() != null && customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email " + customerRequest.getEmail() + " already exists");
        }

        // Check if customer with same phone already exists
        if (customerRequest.getPhone() != null && customerRepository.existsByPhone(customerRequest.getPhone())) {
            throw new CustomerAlreadyExistsException("Customer with phone " + customerRequest.getPhone() + " already exists");
        }

        Customer customer = modelMapper.map(customerRequest, Customer.class);
        customer.setId(UUID.randomUUID());
        customer.setLoyaltyPoints(0);
        customer.setCustomerType("REGULAR");

        Customer savedCustomer = customerRepository.save(customer);

        // Record the activity
        customerActivityService.recordActivity(savedCustomer.getId(), "REGISTRATION", null);

        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Transactional
    public CustomerDTO updateCustomer(UUID id, CustomerRequest customerRequest) {
        log.debug("Updating customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        // Check email uniqueness
        if (customerRequest.getEmail() != null && !customerRequest.getEmail().equals(existingCustomer.getEmail())
                && customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email " + customerRequest.getEmail() + " already exists");
        }

        // Check phone uniqueness
        if (customerRequest.getPhone() != null && !customerRequest.getPhone().equals(existingCustomer.getPhone())
                && customerRepository.existsByPhone(customerRequest.getPhone())) {
            throw new CustomerAlreadyExistsException("Customer with phone " + customerRequest.getPhone() + " already exists");
        }

        // Update fields
        if (customerRequest.getFirstName() != null) {
            existingCustomer.setFirstName(customerRequest.getFirstName());
        }

        if (customerRequest.getLastName() != null) {
            existingCustomer.setLastName(customerRequest.getLastName());
        }

        if (customerRequest.getEmail() != null) {
            existingCustomer.setEmail(customerRequest.getEmail());
        }

        if (customerRequest.getPhone() != null) {
            existingCustomer.setPhone(customerRequest.getPhone());
        }

        if (customerRequest.getAddress() != null) {
            existingCustomer.setAddress(customerRequest.getAddress());
        }

        if (customerRequest.getBirthDate() != null) {
            existingCustomer.setBirthDate(customerRequest.getBirthDate());
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        // Record the activity
        customerActivityService.recordActivity(updatedCustomer.getId(), "PROFILE_UPDATE", null);

        return modelMapper.map(updatedCustomer, CustomerDTO.class);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        log.debug("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }

        customerRepository.deleteById(id);
    }

    @Transactional
    public CustomerDTO updateLoyaltyPoints(UUID id, Integer points) {
        log.debug("Updating loyalty points for customer ID: {}, points: {}", id, points);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        // Update points
        int currentPoints = customer.getLoyaltyPoints();
        int newPoints = currentPoints + points;
        customer.setLoyaltyPoints(Math.max(0, newPoints)); // Ensure points don't go negative

        // Update customer type based on points if needed
        if (newPoints >= 1000 && !customer.getCustomerType().equals("VIP")) {
            customer.setCustomerType("VIP");
            log.info("Customer {} upgraded to VIP status", id);
        } else if (newPoints >= 500 && !customer.getCustomerType().equals("GOLD")
                && !customer.getCustomerType().equals("VIP")) {
            customer.setCustomerType("GOLD");
            log.info("Customer {} upgraded to GOLD status", id);
        }

        Customer updatedCustomer = customerRepository.save(customer);

        // Record the activity
        String activityData = String.format("{\"previousPoints\":%d,\"change\":%d,\"newPoints\":%d}",
                currentPoints, points, updatedCustomer.getLoyaltyPoints());
        customerActivityService.recordActivity(id, "LOYALTY_POINTS_UPDATE", activityData);

        return modelMapper.map(updatedCustomer, CustomerDTO.class);
    }
}