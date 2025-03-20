package com.bookstore.v1.service;

import com.bookstore.v1.entity.Authors;
import com.bookstore.v1.entity.Categories;
import com.bookstore.v1.repository.AuthorsRepository;
import com.bookstore.v1.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    private AuthorsRepository authorsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    public Authors getAuthorName(String name){
        return authorsRepository.findByName(name);
    }

    public Categories getCategoriesName(String name){
        return categoriesRepository.findByName(name);
    }
}
