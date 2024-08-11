package org.example.mvcpageable.service;

import org.example.mvcpageable.entity.Author;
import org.example.mvcpageable.entity.Book;
import org.example.mvcpageable.exception.BookAlreadyExistsException;
import org.example.mvcpageable.exception.BookNotFoundException;
import org.example.mvcpageable.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void testGetBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(new ArrayList<>());

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<Book> result = bookService.getBooks(pageable);

        assertEquals(bookPage, result);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void testGetBookById_BookFound() {
        Author author = new Author(1L, "Jane", "Doe", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book book = new Book(1L, "Book Title", authors);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertEquals(book, result);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testGetBookById_BookNotFound() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });
    }

    @Test
    public void testSaveBook() {
        Author author = new Author(1L, "Jane", "Doe", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book book = new Book(1L, "Book Title", authors);

        Mockito.when(bookRepository.findByTitle("Book Title")).thenReturn(Optional.empty());
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        bookService.saveBook(book);

        Mockito.verify(bookRepository, Mockito.times(1)).findByTitle("Book Title");
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    public void testSaveBook_BookAlreadyExists() {
        Author author = new Author(1L, "Jane", "Doe", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book book = new Book(1L, "Book Title", authors);

        Mockito.when(bookRepository.findByTitle("Book Title")).thenReturn(Optional.of(book));

        assertThrows(BookAlreadyExistsException.class, () -> {
            bookService.saveBook(book);
        });
    }

    @Test
    public void testUpdateBook() {
        Author author = new Author(1L, "Jane", "Doe", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book existingBook = new Book(1L, "Old Title", authors);
        Book updatedBook = new Book(1L, "Updated Book", authors);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        Mockito.when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        bookService.updateBook(1L, updatedBook);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).save(existingBook);
    }

    @Test
    public void testUpdateBook_BookNotFound() {
        Author author = new Author(1L, "Jane", "Doe", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book updatedBook = new Book(1L, "Updated Book", authors);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(1L, updatedBook);
        });
    }

    @Test
    public void testDeleteBook_BookExists() {
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        Mockito.verify(bookRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteBook_BookNotFound() {
        Mockito.when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });
    }
}