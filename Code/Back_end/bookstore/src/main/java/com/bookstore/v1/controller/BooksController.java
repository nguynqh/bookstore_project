package com.bookstore.v1.controller;

import com.bookstore.v1.entity.Books;
import com.bookstore.v1.service.BooksService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/books")
public class BooksController {

    private final BooksService booksService;

    @GetMapping
    public List<Books> getAllBooks(){
        return booksService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Long id){
        Optional<Books> book = booksService.getBookById(id);
        if (book.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book.get());
    }

    @PostMapping
    public Books saveBook(@RequestBody Books books){
        return booksService.saveBook(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable Long id, @RequestBody Books updatedBook){
        Optional<Books> book = booksService.getBookById(id);
        if (book.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        updatedBook.setId(id);
        return ResponseEntity.ok(booksService.saveBook(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        Optional<Books> book = booksService.getBookById(id);
        if (book.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        booksService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Books> searchBook(@RequestParam String query) {
//        need search logic
        return booksService.getAllBooks();
    }
}
