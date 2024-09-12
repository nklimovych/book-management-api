package com.book.management.service.impl;

import com.book.management.dto.BookResponseDto;
import com.book.management.dto.BookSearchParametersDto;
import com.book.management.dto.CreateBookRequestDto;
import com.book.management.exception.DuplicateIsbnException;
import com.book.management.exception.EntityNotFoundException;
import com.book.management.mapper.BookMapper;
import com.book.management.model.Book;
import com.book.management.repository.book.BookRepository;
import com.book.management.repository.book.BookSpecificationBuilder;
import com.book.management.service.BookService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String EXISTING_ISBN_MESSAGE = "There is already a book with ISBN: ";
    private static final String NO_BOOK_WITH_ID_MESSAGE = "There is no book with id: ";

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;

    @Override
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        String isbn = requestDto.getIsbn();
        if (bookRepository.findBookByIsbn(isbn).isPresent()) {
            throw new DuplicateIsbnException(EXISTING_ISBN_MESSAGE + isbn);
        }

        Book book = bookMapper.toEntity(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                             .map(bookMapper::toDto)
                             .toList();
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException(NO_BOOK_WITH_ID_MESSAGE + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(NO_BOOK_WITH_ID_MESSAGE + id));

        String requestIsbn = requestDto.getIsbn();
        if (!Objects.equals(existingBook.getIsbn(), requestIsbn)
                && bookRepository.findBookByIsbn(requestIsbn).isPresent()) {
            throw new DuplicateIsbnException(EXISTING_ISBN_MESSAGE + requestIsbn);
        }

        Book book = bookMapper.toEntity(requestDto);
        book.setId(id);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookResponseDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> specification = specificationBuilder.build(params);
        return bookRepository.findAll(specification, pageable)
                             .stream()
                             .map(bookMapper::toDto)
                             .toList();
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
