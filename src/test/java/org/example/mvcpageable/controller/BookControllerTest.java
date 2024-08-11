package org.example.mvcpageable.controller;

import org.example.mvcpageable.entity.Author;
import org.example.mvcpageable.entity.Book;
import org.example.mvcpageable.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void testGetAllBooks() throws Exception {
        Page<Book> bookPage = new PageImpl<>(new ArrayList<>());

        Mockito.when(bookService.getBooks(Mockito.any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        Mockito.verify(bookService, Mockito.times(1)).getBooks(Mockito.any(Pageable.class));
    }

    @Test
    public void testGetBookById() throws Exception {
        Author author = new Author(1L, "Олег", "Олегов", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book book = new Book(1L, "Название книги", authors);

        Mockito.when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Название книги"))
                .andExpect(jsonPath("$.authors[0].name").value("Олег"))
                .andExpect(jsonPath("$.authors[0].surname").value("Олегов"));

        Mockito.verify(bookService, Mockito.times(1)).getBookById(1L);
    }

    @Test
    public void testCreateBook() throws Exception {
        Author author = new Author(1L, "Олег", "Олегов", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book book = new Book(1L, "Название книги", authors);

        Mockito.doNothing().when(bookService).saveBook(Mockito.any(Book.class));

        String bookJson = "{ \"title\": \"Название книги\", \"authors\": [{ \"name\": \"Олег\", \"surname\": \"Олегов\" }] }";

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Книга добавлена"));

        Mockito.verify(bookService, Mockito.times(1)).saveBook(Mockito.any(Book.class));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Author author = new Author(1L, "Олег", "Олегов", null);
        List<Author> authors = new ArrayList<>();
        authors.add(author);

        Book updatedBook = new Book(1L, "Название книги", authors);

        Mockito.doNothing().when(bookService).updateBook(Mockito.eq(1L), Mockito.any(Book.class));

        String bookJson = "{ \"title\": \"Название книги\", \"authors\": [{ \"name\": \"Олег\", \"surname\": \"Олегов\" }] }";

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("Книга обновлена"));

        Mockito.verify(bookService, Mockito.times(1)).updateBook(Mockito.eq(1L), Mockito.any(Book.class));
    }

    @Test
    public void testDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        Mockito.verify(bookService, Mockito.times(1)).deleteBook(1L);
    }
}