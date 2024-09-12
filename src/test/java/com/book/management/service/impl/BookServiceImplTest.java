package com.book.management.service.impl;

import static com.book.management.util.TestConstants.EXPECTED_LIST_SIZE;
import static com.book.management.util.TestConstants.INVALID_ID;
import static com.book.management.util.TestConstants.ISBN_DUPLICATE;
import static com.book.management.util.TestConstants.NUMBER_OF_INVOCATIONS;
import static com.book.management.util.TestConstants.PAGE_NUMBER;
import static com.book.management.util.TestConstants.PAGE_SIZE;
import static com.book.management.util.TestConstants.VALID_BOOK_AUTHOR;
import static com.book.management.util.TestConstants.VALID_BOOK_AUTHOR_NOT_IN_DB;
import static com.book.management.util.TestConstants.VALID_BOOK_GENRE_POETRY;
import static com.book.management.util.TestConstants.VALID_BOOK_ID;
import static com.book.management.util.TestConstants.VALID_BOOK_ISBN;
import static com.book.management.util.TestConstants.VALID_BOOK_PUBLICATION_YEAR;
import static com.book.management.util.TestConstants.VALID_BOOK_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.management.dto.BookResponseDto;
import com.book.management.dto.BookSearchParametersDto;
import com.book.management.dto.CreateBookRequestDto;
import com.book.management.exception.DuplicateIsbnException;
import com.book.management.exception.EntityNotFoundException;
import com.book.management.mapper.BookMapper;
import com.book.management.model.Book;
import com.book.management.repository.SpecificationProviderManager;
import com.book.management.repository.book.BookRepository;
import com.book.management.repository.book.BookSpecificationBuilder;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @Mock
    private SpecificationProviderManager<Book> specificationProviderManager;

    private Book validBook;

    @BeforeEach
    void setUp() {
        validBook = new Book();
        validBook.setId(VALID_BOOK_ID);
        validBook.setTitle(VALID_BOOK_TITLE);
        validBook.setAuthor(VALID_BOOK_AUTHOR);
        validBook.setPublicationYear(VALID_BOOK_PUBLICATION_YEAR);
        validBook.setGenre(VALID_BOOK_GENRE_POETRY);
        validBook.setIsbn(VALID_BOOK_ISBN);
    }

    @Test
    @DisplayName("Save a book -> (valid request)")
    void save_ValidBook_ReturnBookDto() {
        CreateBookRequestDto createBookDto = getCreateBookRequestDto();
        BookResponseDto bookDto = getBookDto(createBookDto);

        when(bookMapper.toEntity(createBookDto)).thenReturn(validBook);
        when(bookRepository.save(validBook)).thenReturn(validBook);
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        BookResponseDto savedBook = bookService.save(createBookDto);

        assertEquals(bookDto, savedBook);
    }

    @Test
    @DisplayName("Save a book -> (duplicate isbn)")
    void save_BookWithDuplicate_Isbn_ThrowException() {
        CreateBookRequestDto createBookDto = getCreateBookRequestDto();
        String expectedErrorMessage = "There is already a book with ISBN: " + VALID_BOOK_ISBN;

        when(bookRepository.findBookByIsbn(VALID_BOOK_ISBN)).thenReturn(Optional.of(new Book()));

        DuplicateIsbnException exception =
                assertThrows(DuplicateIsbnException.class, () -> bookService.save(createBookDto));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Find all books -> (valid request)")
    void findAll_ValidRequest_ReturnListOfBookDto() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookResponseDto bookDto = getBookDto(getCreateBookRequestDto());

        Page<Book> bookPage = new PageImpl<>(List.of(validBook));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        List<BookResponseDto> books = bookService.findAll(pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(bookDto, books.getFirst());
    }

    @Test
    @DisplayName("Find book by id -> (valid request)")
    void findById_ValidRequest_ReturnBookDto() {
        BookResponseDto bookDto = getBookDto(getCreateBookRequestDto());

        when(bookRepository.findBookById(VALID_BOOK_ID)).thenReturn(Optional.of(validBook));
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        BookResponseDto foundBook = bookService.findById(VALID_BOOK_ID);

        assertEquals(bookDto, foundBook);
    }

    @Test
    @DisplayName("Find book by id -> (not existent book)")
    void findById_NonExistentBook_ThrowException() {
        when(bookRepository.findBookById(VALID_BOOK_ID)).thenReturn(Optional.empty());
        String expectedErrorMessage = "There is no book with id: " + VALID_BOOK_ID;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookService.findById(VALID_BOOK_ID));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Update book by id -> (valid request)")
    void updateById_ValidRequest_ReturnBookDto() {
        Book updatedBook = new Book();
        updatedBook.setId(VALID_BOOK_ID);

        CreateBookRequestDto updateBookDto = getCreateBookRequestDto();
        BookResponseDto updatedBookDto = getBookDto(updateBookDto);

        when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(validBook));
        when(bookMapper.toEntity(updateBookDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDto);

        BookResponseDto result = bookService.updateById(VALID_BOOK_ID, updateBookDto);

        assertEquals(updatedBookDto, result);
    }

    @Test
    @DisplayName("Update book by id -> (not existent book)")
    void updateById_NoBookInDatabase_ThrowException() {
        String expectedErrorMessage = "There is no book with id: " + INVALID_ID;

        when(bookRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookService.updateById(INVALID_ID, getCreateBookRequestDto()));

        assertEquals(expectedErrorMessage, exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Update book by id -> (duplicate ISBN)")
    void updateById_DuplicateIsbn_ThrowException() {
        CreateBookRequestDto updateBookDto = getCreateBookRequestDto();
        updateBookDto.setIsbn(ISBN_DUPLICATE);

        String expectedErrorMessage = "There is already a book with ISBN: " + ISBN_DUPLICATE;

        when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(validBook));
        when(bookRepository.findBookByIsbn(ISBN_DUPLICATE)).thenReturn(Optional.of(validBook));

        DuplicateIsbnException exception = assertThrows(DuplicateIsbnException.class, () ->
                bookService.updateById(VALID_BOOK_ID, updateBookDto));

        assertEquals(expectedErrorMessage, exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Search books -> (valid request)")
    void search_ValidParameters_ReturnListOfBookDto() {
        BookSearchParametersDto params = new BookSearchParametersDto(
                VALID_BOOK_TITLE,
                VALID_BOOK_AUTHOR,
                VALID_BOOK_GENRE_POETRY
        );

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookResponseDto bookDto = getBookDto(getCreateBookRequestDto());

        Specification<Book> specification =
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Page<Book> bookPage = new PageImpl<>(List.of(validBook));

        when(specificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        List<BookResponseDto> books = bookService.search(params, pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(bookDto, books.getFirst());
    }

    @Test
    @DisplayName("Search books -> (valid request)")
    void search_ByAuthor_ReturnListOfBookDto() {
        BookSearchParametersDto params = new BookSearchParametersDto(
                null, VALID_BOOK_AUTHOR, null);
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        BookResponseDto bookDto = getBookDto(getCreateBookRequestDto());

        Specification<Book> specification =
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Page<Book> bookPage = new PageImpl<>(List.of(validBook));

        when(specificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(validBook)).thenReturn(bookDto);

        List<BookResponseDto> books = bookService.search(params, pageable);

        assertEquals(EXPECTED_LIST_SIZE, books.size());
        assertEquals(bookDto, books.getFirst());
    }

    @Test
    @DisplayName("Search books -> (no matching books)")
    void search_NoMatchingBooks_ReturnsEmptyList() {
        BookSearchParametersDto params = new BookSearchParametersDto(
                null, VALID_BOOK_AUTHOR_NOT_IN_DB, null);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        Page<Book> emptyBookPage = new PageImpl<>(List.of());

        Specification<Book> specification =
                (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        when(specificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(emptyBookPage);

        List<BookResponseDto> books = bookService.search(params, pageable);

        assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("Delete book by id -> (valid request)")
    void delete_ValidRequest_DeleteBook() {
        bookService.delete(VALID_BOOK_ID);
        verify(bookRepository, times(NUMBER_OF_INVOCATIONS)).deleteById(VALID_BOOK_ID);
    }

    private CreateBookRequestDto getCreateBookRequestDto() {
        CreateBookRequestDto createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle(VALID_BOOK_TITLE);
        createBookDto.setAuthor(VALID_BOOK_AUTHOR);
        createBookDto.setIsbn(VALID_BOOK_ISBN);
        createBookDto.setPublicationYear(VALID_BOOK_PUBLICATION_YEAR);
        createBookDto.setGenre(VALID_BOOK_GENRE_POETRY);
        return createBookDto;
    }

    private BookResponseDto getBookDto(CreateBookRequestDto dto) {
        BookResponseDto bookDto = new BookResponseDto();
        bookDto.setTitle(dto.getTitle());
        bookDto.setAuthor(dto.getAuthor());
        bookDto.setIsbn(dto.getIsbn());
        bookDto.setGenre(dto.getGenre());
        bookDto.setPublicationYear(dto.getPublicationYear());
        return bookDto;
    }
}
