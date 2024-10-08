package org.example.mvcpageable.controller;

import org.example.mvcpageable.entity.Book;
import org.example.mvcpageable.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(Pageable pageable) {

        return new ResponseEntity<>(bookService.getBooks(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        bookService.saveBook(book);
        return new ResponseEntity<>("Книга добавлена", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) {
        bookService.updateBook(id, book);

        return new ResponseEntity<>("Книга обновлена", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);

        return new ResponseEntity<>("Книга удалена",HttpStatus.NO_CONTENT);
    }
}
