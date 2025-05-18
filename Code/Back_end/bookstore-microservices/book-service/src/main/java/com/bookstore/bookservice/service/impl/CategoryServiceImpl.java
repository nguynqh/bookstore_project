package com.bookstore.bookservice.service.impl;

import com.bookstore.bookservice.dto.CategoryDTO;
import com.bookstore.bookservice.dto.CategoryRequest;
import com.bookstore.bookservice.dto.PagedResponse;
import com.bookstore.bookservice.entity.Category;
import com.bookstore.bookservice.exception.CategoryNotFoundException;
import com.bookstore.bookservice.exception.ResourceAlreadyExistsException;
import com.bookstore.bookservice.repository.CategoryRepository;
import com.bookstore.bookservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PagedResponse<CategoryDTO> getAllCategoriesPaged(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        return PagedResponse.<CategoryDTO>builder()
                .content(categoryDTOs)
                .page(categoryPage.getNumber())
                .size(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .last(categoryPage.isLast())
                .build();
    }

    @Override
    public CategoryDTO getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryRequest categoryRequest) {
        // Check if category with the same name already exists
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ResourceAlreadyExistsException("Category with name " + categoryRequest.getName() + " already exists");
        }

        Category category = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(UUID id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        // Check name uniqueness if changed
        if (!category.getName().equals(categoryRequest.getName()) &&
                categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ResourceAlreadyExistsException("Category with name " + categoryRequest.getName() + " already exists");
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        // In a real application, you might want to check if the category is being used by books
        // and handle that case appropriately (either prevent deletion or cascade delete/update)

        categoryRepository.delete(category);
    }
}