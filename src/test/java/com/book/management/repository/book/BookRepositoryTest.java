package com.book.management.repository.book;

import static com.book.management.util.TestConstants.AUTHOR_PARAM_NAME;
import static com.book.management.util.TestConstants.EMPTY_STRING;
import static com.book.management.util.TestConstants.GENRE_PARAM_NAME;
import static com.book.management.util.TestConstants.INVALID_BOOK_ISBN;
import static com.book.management.util.TestConstants.INVALID_ID;
import static com.book.management.util.TestConstants.NEGATIVE_PAGE_SIZE;
import static com.book.management.util.TestConstants.PAGE_NUMBER;
import static com.book.management.util.TestConstants.PAGE_SIZE;
import static com.book.management.util.TestConstants.TEST_DB;
import static com.book.management.util.TestConstants.VALID_BOOK_AUTHOR;
import static com.book.management.util.TestConstants.VALID_BOOK_AUTHOR_NOT_IN_DB;
import static com.book.management.util.TestConstants.VALID_BOOK_GENRE_POETRY;
import static com.book.management.util.TestConstants.VALID_BOOK_ID;
import static com.book.management.util.TestConstants.VALID_BOOK_ISBN;
import static com.book.management.util.TestConstants.VALID_BOOK_ISBN_NOT_IN_DB;
import static com.book.management.util.TestConstants.VALID_BOOK_PUBLICATION_YEAR;
import static com.book.management.util.TestConstants.VALID_BOOK_TITLE;
import static com.book.management.util.TestConstants.VALID_GENRE_IN_DIFFERENT_CASES_POETRY;
import static com.book.management.util.TestConstants.ZERO_PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.book.management.model.Book;
import jakarta.persistence.criteria.Expression;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

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
    @DisplayName("Find all books -> (valid request)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_ValidRequest_ReturnsNonEmptyListOfBooks() {
        Page<Book> books = bookRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    @DisplayName("Find all books with specification -> (valid genre)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_ValidSpecification_ReturnsFilteredBooks() {
        Specification<Book> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(GENRE_PARAM_NAME), VALID_BOOK_GENRE_POETRY);
        Page<Book> books = bookRepository.findAll(spec, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        assertNotNull(books);
        assertFalse(books.isEmpty());
        books.forEach(book -> assertEquals(VALID_BOOK_GENRE_POETRY, book.getGenre()));
    }

    @Test
    @DisplayName("Find all books with specification -> (valid genre in different cases)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_ValidSpecificationInDifferentCases_ReturnsFilteredBooks() {
        Specification<Book> spec = (root, query, criteriaBuilder) -> {
            Expression<String> lowerCaseGenre = criteriaBuilder.lower(root.get(GENRE_PARAM_NAME));

            return criteriaBuilder.equal(
                    lowerCaseGenre,
                    VALID_GENRE_IN_DIFFERENT_CASES_POETRY.toLowerCase()
            );
        };
        Page<Book> books = bookRepository.findAll(spec, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        assertNotNull(books);
        assertFalse(books.isEmpty());
        books.forEach(book -> assertEquals(VALID_BOOK_GENRE_POETRY, book.getGenre()));
    }

    @Test
    @DisplayName("Find all books with specification -> (not existent author)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_NoMatchingBooks_ReturnsEmptyPage() {
        Specification<Book> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(AUTHOR_PARAM_NAME), VALID_BOOK_AUTHOR_NOT_IN_DB);
        Page<Book> books = bookRepository.findAll(spec, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("Find all books with specification -> (null specification)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_NullSpecification_ReturnsAllBooks() {
        Page<Book> books = bookRepository.findAll(
                (Specification<Book>) null,
                PageRequest.of(PAGE_NUMBER, PAGE_SIZE)
        );

        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    @DisplayName("Find all books -> (zero page size)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_ZeroPageSize_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                bookRepository.findAll(PageRequest.of(PAGE_NUMBER, ZERO_PAGE_SIZE)));
    }

    @Test
    @DisplayName("Find all books -> (negative page size)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAll_NegativePageSize_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                bookRepository.findAll(PageRequest.of(PAGE_NUMBER, NEGATIVE_PAGE_SIZE)));
    }

    @Test
    @DisplayName("Find a book by isbn -> (valid isbn)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_ValidIsbn_ReturnsBookWithMatchingIsbn() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(VALID_BOOK_ISBN);

        assertTrue(bookOptional.isPresent());
        assertEquals(validBook, bookOptional.get());
    }

    @Test
    @DisplayName("Find a book by isbn -> (invalid isbn)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_InvalidIsbn_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(INVALID_BOOK_ISBN);

        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find a book by isbn -> (empty isbn)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_EmptyIsbn_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(EMPTY_STRING);

        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find a book by isbn -> (null isbn)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_NullIsbn_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(null);

        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find book by id -> (existing book)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookById_ValidId_ReturnsBookWithMatchingId() {
        Book book = bookRepository.findBookById(VALID_BOOK_ID).orElse(null);

        assertNotNull(book);
        assertEquals(validBook, book);
    }

    @Test
    @DisplayName("Find a book by isbn -> (valid isbn, not in DB)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_ValidIsbnBookNotInRepository_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(VALID_BOOK_ISBN_NOT_IN_DB);

        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find book by id -> (non existing book)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookById_InvalidId_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookById(INVALID_ID);

        assertFalse(bookOptional.isPresent());
    }

    @Test
    @DisplayName("Find book by id -> (null id)")
    @Sql(scripts = {TEST_DB}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookById_NullId_ReturnsEmptyOptional() {
        Optional<Book> bookOptional = bookRepository.findBookById(null);

        assertFalse(bookOptional.isPresent());
    }
}
