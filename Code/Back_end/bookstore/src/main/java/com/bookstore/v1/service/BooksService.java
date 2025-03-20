package com.bookstore.v1.service;

import com.bookstore.v1.entity.Books;
import com.bookstore.v1.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Books> getAllBooks(){
        return booksRepository.findAll();
    }

    public Optional<Books> getBookById(Long id){
        return booksRepository.findById(id);
    }

    public Books saveBook(Books books){
        return booksRepository.save(books);
    }

    public void deleteBook(Long id){
        booksRepository.deleteById(id);
    }
}
