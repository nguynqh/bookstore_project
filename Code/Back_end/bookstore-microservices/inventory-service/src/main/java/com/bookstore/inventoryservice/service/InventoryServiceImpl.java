package com.bookstore.inventoryservice.service;

import com.bookstore.inventoryservice.dto.BookDTO;
import com.bookstore.inventoryservice.dto.InventoryDTO;
import com.bookstore.inventoryservice.dto.InventoryTransactionDTO;
import com.bookstore.inventoryservice.dto.InventoryUpdateRequest;
import com.bookstore.inventoryservice.entity.Inventory;
import com.bookstore.inventoryservice.entity.InventoryTransaction;
import com.bookstore.inventoryservice.event.InventoryUpdatedEvent;
import com.bookstore.inventoryservice.exception.InsufficientStockException;
import com.bookstore.inventoryservice.exception.InventoryNotFoundException;
import com.bookstore.inventoryservice.repository.InventoryRepository;
import com.bookstore.inventoryservice.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestTemplate restTemplate;

    @Value("${bookstore.book-service.url}")
    private String bookServiceUrl;

    @Value("${bookstore.inventory.threshold}")
    private Integer defaultThreshold;

    @Value("${bookstore.kafka.topics.inventory-updated}")
    private String inventoryUpdatedTopic;

    @Override
    public Page<InventoryDTO> getAllInventory(Pageable pageable) {
        Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);
        return inventoryPage.map(inventory -> {
            InventoryDTO dto = modelMapper.map(inventory, InventoryDTO.class);
            try {
                BookDTO bookDTO = restTemplate.getForObject(
                        bookServiceUrl + "/books/" + inventory.getBookId(),
                        BookDTO.class
                );
                if (bookDTO != null) {
                    dto.setBookTitle(bookDTO.getTitle());
                }
            } catch (Exception e) {
                log.warn("Could not fetch book details for inventory ID: {}", inventory.getId(), e);
            }
            dto.setLowStock(inventory.getQuantity() <= inventory.getThreshold());
            return dto;
        });
    }

    @Override
    public InventoryDTO getInventoryByBookId(UUID bookId) {
        Inventory inventory = inventoryRepository.findByBookId(bookId)
                .orElseThrow(() -> new InventoryNotFoundException(bookId));

        InventoryDTO dto = modelMapper.map(inventory, InventoryDTO.class);
        try {
            BookDTO bookDTO = restTemplate.getForObject(
                    bookServiceUrl + "/books/" + bookId,
                    BookDTO.class
            );
            if (bookDTO != null) {
                dto.setBookTitle(bookDTO.getTitle());
            }
        } catch (Exception e) {
            log.warn("Could not fetch book details for book ID: {}", bookId, e);
        }
        dto.setLowStock(inventory.getQuantity() <= inventory.getThreshold());
        return dto;
    }

    @Override
    public boolean checkAvailability(UUID bookId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByBookId(bookId)
                .orElseThrow(() -> new InventoryNotFoundException(bookId));

        return inventory.getQuantity() >= quantity;
    }

    @Override
    @Transactional
    public InventoryDTO updateInventory(InventoryUpdateRequest request) {
        Inventory inventory = inventoryRepository.findByBookId(request.getBookId())
                .orElseThrow(() -> new InventoryNotFoundException(request.getBookId()));

        Integer previousQuantity = inventory.getQuantity();
        Integer newQuantity;

        if (request.getTransactionType() == InventoryTransaction.TransactionType.IN) {
            newQuantity = inventory.getQuantity() + request.getQuantity();
            inventory.setLastRestock(LocalDateTime.now());
        } else {
            if (inventory.getQuantity() < request.getQuantity()) {
                throw new InsufficientStockException(
                        request.getBookId(),
                        request.getQuantity(),
                        inventory.getQuantity()
                );
            }
            newQuantity = inventory.getQuantity() - request.getQuantity();
        }

        inventory.setQuantity(newQuantity);
        inventory = inventoryRepository.save(inventory);

        // Record the transaction
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setId(UUID.randomUUID());
        transaction.setBookId(request.getBookId());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setQuantity(request.getQuantity());
        transaction.setReason(request.getReason());
        transaction.setReferenceId(request.getReferenceId());
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Publish event for inventory update
        InventoryUpdatedEvent event = InventoryUpdatedEvent.builder()
                .bookId(request.getBookId())
                .newQuantity(newQuantity)
                .previousQuantity(previousQuantity)
                .lowStock(newQuantity <= inventory.getThreshold())
                .updatedAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(inventoryUpdatedTopic, request.getBookId().toString(), event);

        InventoryDTO dto = modelMapper.map(inventory, InventoryDTO.class);
        dto.setLowStock(inventory.getQuantity() <= inventory.getThreshold());
        return dto;
    }

    @Override
    @Transactional
    public InventoryDTO initializeInventory(UUID bookId, Integer initialQuantity, Integer threshold, String location) {
        if (inventoryRepository.existsByBookId(bookId)) {
            log.warn("Inventory already exists for book ID: {}", bookId);
            return getInventoryByBookId(bookId);
        }

        Inventory inventory = new Inventory();
        inventory.setId(UUID.randomUUID());
        inventory.setBookId(bookId);
        inventory.setQuantity(initialQuantity);
        inventory.setThreshold(threshold != null ? threshold : defaultThreshold);
        inventory.setLocation(location);
        inventory.setLastRestock(LocalDateTime.now());

        inventory = inventoryRepository.save(inventory);

        // Record the transaction
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setId(UUID.randomUUID());
        transaction.setBookId(bookId);
        transaction.setTransactionType(InventoryTransaction.TransactionType.IN);
        transaction.setQuantity(initialQuantity);
        transaction.setReason("Initial stock");
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        InventoryDTO dto = modelMapper.map(inventory, InventoryDTO.class);
        dto.setLowStock(inventory.getQuantity() <= inventory.getThreshold());
        return dto;
    }

    @Override
    public List<InventoryDTO> getLowStockItems() {
        List<Inventory> lowStockItems = inventoryRepository.findAll().stream()
                .filter(inventory -> inventory.getQuantity() <= inventory.getThreshold())
                .collect(Collectors.toList());

        return lowStockItems.stream()
                .map(inventory -> {
                    InventoryDTO dto = modelMapper.map(inventory, InventoryDTO.class);
                    try {
                        BookDTO bookDTO = restTemplate.getForObject(
                                bookServiceUrl + "/books/" + inventory.getBookId(),
                                BookDTO.class
                        );
                        if (bookDTO != null) {
                            dto.setBookTitle(bookDTO.getTitle());
                        }
                    } catch (Exception e) {
                        log.warn("Could not fetch book details for book ID: {}", inventory.getBookId(), e);
                    }
                    dto.setLowStock(true);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<InventoryTransactionDTO> getTransactionHistory(UUID bookId, Pageable pageable) {
        Page<InventoryTransaction> transactions = transactionRepository.findByBookId(bookId, pageable);

        return transactions.map(transaction -> {
            InventoryTransactionDTO dto = modelMapper.map(transaction, InventoryTransactionDTO.class);
            try {
                BookDTO bookDTO = restTemplate.getForObject(
                        bookServiceUrl + "/books/" + transaction.getBookId(),
                        BookDTO.class
                );
                if (bookDTO != null) {
                    dto.setBookTitle(bookDTO.getTitle());
                }
            } catch (Exception e) {
                log.warn("Could not fetch book details for book ID: {}", transaction.getBookId(), e);
            }
            return dto;
        });
    }

    @Override
    public List<InventoryTransactionDTO> getTransactionsByReference(UUID referenceId) {
        List<InventoryTransaction> transactions = transactionRepository.findByReferenceId(referenceId);

        return transactions.stream()
                .map(transaction -> {
                    InventoryTransactionDTO dto = modelMapper.map(transaction, InventoryTransactionDTO.class);
                    try {
                        BookDTO bookDTO = restTemplate.getForObject(
                                bookServiceUrl + "/books/" + transaction.getBookId(),
                                BookDTO.class
                        );
                        if (bookDTO != null) {
                            dto.setBookTitle(bookDTO.getTitle());
                        }
                    } catch (Exception e) {
                        log.warn("Could not fetch book details for book ID: {}", transaction.getBookId(), e);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}