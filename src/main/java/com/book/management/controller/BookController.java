package com.book.management.controller;

import com.book.management.dto.BookResponseDto;
import com.book.management.dto.BookSearchParametersDto;
import com.book.management.dto.CreateBookRequestDto;
import com.book.management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Book management API", description = "Endpoints for book management")
@RequestMapping(value = "/api/books")
public class BookController {
    private static final int DEFAULT_PAGE_SIZE = 5;

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book", description = "Create a new book with generated ID")
    public BookResponseDto createBook(
            @RequestBody @Valid CreateBookRequestDto requestDto
    ) {
        return bookService.save(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all books", description = "Get a sorted list of all books")
    public List<BookResponseDto> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = {"author", "title"},
                    value = DEFAULT_PAGE_SIZE)
            Pageable pageable
    ) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a book by ID", description = "Get a book by its ID, if exists")
    public BookResponseDto getBookById(
            @PathVariable Long id
    ) {
        return bookService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update a book by ID", description = "Update a book by its ID, if exists")
    public BookResponseDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid CreateBookRequestDto requestDto
    ) {
        return bookService.updateById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book by ID", description = "Delete a book by its ID, if exists")
    public void deleteBook(
            @PathVariable Long id
    ) {
        bookService.delete(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of books by search parameters",
            description = "Get a list of books by search parameters: title, author and genre")
    public List<BookResponseDto> searchBook(
            @ParameterObject
            @PageableDefault(
                    sort = {"author", "title"},
                    value = DEFAULT_PAGE_SIZE)
            Pageable pageable,
            BookSearchParametersDto parametersDto
    ) {
        return bookService.search(parametersDto, pageable);
    }
}
