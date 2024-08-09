package org.example.mvcpageable.service;

import org.example.mvcpageable.entity.Book;
import org.example.mvcpageable.exception.BookAlreadyExistsException;
import org.example.mvcpageable.exception.BookNotFoundException;
import org.example.mvcpageable.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Page<Book> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(Long id) {
        Optional<Book> optBook = bookRepository.findById(id);
        if (optBook.isEmpty()) {
            throw new BookNotFoundException("Книга не найдена");
        }

        return optBook.get();
    }

    public void saveBook(Book book) {
        if (bookRepository.findByTitle(book.getTitle()).isPresent()) {
            throw new BookAlreadyExistsException("Книга с таким названием уже существует");
        }

        bookRepository.save(book);
    }

    public void updateBook(Long bookId, Book book) {
        Book bookFromBd = bookRepository.findById(book.getId())
                                        .orElseThrow(() -> new BookNotFoundException("Книга не найдена"));

        bookFromBd.setTitle(book.getTitle());
        bookFromBd.setAuthors(book.getAuthors());

        bookRepository.save(bookFromBd);
    }

    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Книга не найдена");
        }

        bookRepository.deleteById(bookId);
    }
}
