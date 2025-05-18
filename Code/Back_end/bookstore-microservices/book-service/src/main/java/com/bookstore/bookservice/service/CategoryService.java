package com.bookstore.bookservice.service;

import com.bookstore.bookservice.dto.CategoryDTO;
import com.bookstore.bookservice.dto.CategoryRequest;
import com.bookstore.bookservice.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    PagedResponse<CategoryDTO> getAllCategoriesPaged(Pageable pageable);

    CategoryDTO getCategoryById(UUID id);

    CategoryDTO createCategory(CategoryRequest categoryRequest);

    CategoryDTO updateCategory(UUID id, CategoryRequest categoryRequest);

    void deleteCategory(UUID id);
}