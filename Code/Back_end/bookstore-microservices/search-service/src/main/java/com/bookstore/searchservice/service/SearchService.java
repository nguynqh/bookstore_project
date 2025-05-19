package com.bookstore.searchservice.service;

import com.bookstore.searchservice.document.BookDocument;
import com.bookstore.searchservice.dto.BookSearchDTO;
import com.bookstore.searchservice.dto.SearchRequest;
import com.bookstore.searchservice.dto.SearchResponse;
import com.bookstore.searchservice.repository.BookSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final BookSearchRepository bookSearchRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @Value("${book-service.url}")
    private String bookServiceUrl;

    public SearchResponse search(SearchRequest request) {
        // Create a criteria-based query
        Criteria criteria = new Criteria();

        // Main search query
        if (request.getQuery() != null && !request.getQuery().isEmpty()) {
            criteria = criteria.or(
                    new Criteria("title").contains(request.getQuery()).boost(2.0f)
                            .or("author").contains(request.getQuery()).boost(1.5f)
                            .or("description").contains(request.getQuery())
                            .or("categories.name").contains(request.getQuery()).boost(1.5f)
            );
        }

        // Add filters as criteria
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            criteria = criteria.and(new Criteria("categories.id").in(request.getCategories()));
        }

        if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
            criteria = criteria.and(new Criteria("author").contains(request.getAuthor()));
        }

        if (request.getPublisher() != null && !request.getPublisher().isEmpty()) {
            criteria = criteria.and(new Criteria("publisher").contains(request.getPublisher()));
        }

        if (request.getMinPrice() != null) {
            criteria = criteria.and(new Criteria("sellingPrice").greaterThanEqual(request.getMinPrice()));
        }

        if (request.getMaxPrice() != null) {
            criteria = criteria.and(new Criteria("sellingPrice").lessThanEqual(request.getMaxPrice()));
        }

        if (request.getFromDate() != null) {
            criteria = criteria.and(new Criteria("publicationDate").greaterThanEqual(request.getFromDate()));
        }

        if (request.getToDate() != null) {
            criteria = criteria.and(new Criteria("publicationDate").lessThanEqual(request.getToDate()));
        }

        Query query = new CriteriaQuery(criteria)
                .setPageable(PageRequest.of(request.getPage(), request.getSize()));

        // For more complex sorting, we can use NativeQuery
        if ("title".equals(request.getSortBy()) ||
                "price".equals(request.getSortBy()) ||
                "date".equals(request.getSortBy())) {

            String sortField;
            switch (request.getSortBy()) {
                case "price":
                    sortField = "sellingPrice";
                    break;
                case "date":
                    sortField = "publicationDate";
                    break;
                default:
                    sortField = "title";
                    break;
            }

            Criteria finalCriteria = criteria;
            query = NativeQuery.builder()
                    .withQuery(q -> q.bool(b -> {
                        if (finalCriteria != null) {
                            return b.must(m -> m.queryString(qs -> qs.query(finalCriteria.toString())));
                        }
                        return b;
                    }))
                    .withSort(s -> s.field(f -> f
                            .field(sortField)
                            .order(request.isAscending() ?
                                    co.elastic.clients.elasticsearch._types.SortOrder.Asc :
                                    co.elastic.clients.elasticsearch._types.SortOrder.Desc)))
                    .withPageable(PageRequest.of(request.getPage(), request.getSize()))
                    .build();
        }

        // Execute search
        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query, BookDocument.class);

        // Map results to DTOs
        List<BookSearchDTO> books = searchHits.getSearchHits().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        // Build response
        return new SearchResponse(
                books,
                searchHits.getTotalHits(),
                (int) Math.ceil((double) searchHits.getTotalHits() / request.getSize()),
                request.getPage(),
                request.getSize()
        );
    }

    public BookSearchDTO findById(String id) {
        BookDocument book = bookSearchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found in search index: " + id));
        return mapToDTO(book);
    }

    public void saveBook(BookDocument book) {
        bookSearchRepository.save(book);
    }

    public void deleteBook(String id) {
        bookSearchRepository.deleteById(id);
    }

    public void syncBooksFromBookService() {
        log.info("Starting book synchronization with Book Service");
        try {
            ResponseEntity<BookSearchDTO[]> response = restTemplate.getForEntity(
                    bookServiceUrl + "/books?size=1000",
                    BookSearchDTO[].class
            );

            if (response.getBody() != null) {
                Arrays.stream(response.getBody()).forEach(bookDTO -> {
                    BookDocument bookDocument = modelMapper.map(bookDTO, BookDocument.class);
                    saveBook(bookDocument);
                });
                log.info("Successfully synchronized {} books from Book Service", response.getBody().length);
            }
        } catch (Exception e) {
            log.error("Error synchronizing books from Book Service", e);
        }
    }

    private BookSearchDTO mapToDTO(SearchHit<BookDocument> searchHit) {
        BookDocument book = searchHit.getContent();
        return mapToDTO(book);
    }

    private BookSearchDTO mapToDTO(BookDocument book) {
        BookSearchDTO dto = modelMapper.map(book, BookSearchDTO.class);
        dto.setPrice(book.getSellingPrice());

        if (book.getCategories() != null) {
            dto.setCategories(
                    book.getCategories().stream()
                            .map(category -> modelMapper.map(category, BookSearchDTO.CategoryDTO.class))
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }
}