package com.book.management.controller;

import static com.book.management.util.TestConstants.AUTHOR_PARAM_NAME;
import static com.book.management.util.TestConstants.BASE_URL;
import static com.book.management.util.TestConstants.ERRORS_EXPRESSION;
import static com.book.management.util.TestConstants.GENRE_PARAM_NAME;
import static com.book.management.util.TestConstants.ID;
import static com.book.management.util.TestConstants.INVALID_ID;
import static com.book.management.util.TestConstants.ISBN_PARAM_NAME;
import static com.book.management.util.TestConstants.PUBLICATION_YEAR_PARAM_NAME;
import static com.book.management.util.TestConstants.SEARCH_URL;
import static com.book.management.util.TestConstants.TITLE_0_EXPRESSION;
import static com.book.management.util.TestConstants.TITLE_EXPRESSION;
import static com.book.management.util.TestConstants.TITLE_PARAM_NAME;
import static com.book.management.util.TestConstants.VALID_BOOK_AUTHOR;
import static com.book.management.util.TestConstants.VALID_BOOK_GENRE_POETRY;
import static com.book.management.util.TestConstants.VALID_BOOK_ID;
import static com.book.management.util.TestConstants.VALID_BOOK_ISBN;
import static com.book.management.util.TestConstants.VALID_BOOK_PUBLICATION_YEAR;
import static com.book.management.util.TestConstants.VALID_BOOK_TITLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.management.dto.BookResponseDto;
import com.book.management.dto.BookSearchParametersDto;
import com.book.management.dto.CreateBookRequestDto;
import com.book.management.exception.EntityNotFoundException;
import com.book.management.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Year;
import java.util.Collections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DisplayName("Book Controller Tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookResponseDto bookDto;
    private CreateBookRequestDto createBookDto;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @BeforeEach
    void setup() {
        bookDto = new BookResponseDto();
        bookDto.setId(VALID_BOOK_ID);
        bookDto.setTitle(VALID_BOOK_TITLE);
        bookDto.setAuthor(VALID_BOOK_AUTHOR);
        bookDto.setIsbn(VALID_BOOK_ISBN);
        bookDto.setPublicationYear(VALID_BOOK_PUBLICATION_YEAR);
        bookDto.setGenre(VALID_BOOK_GENRE_POETRY);

        createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle(VALID_BOOK_TITLE);
        createBookDto.setAuthor(VALID_BOOK_AUTHOR);
        createBookDto.setIsbn(VALID_BOOK_ISBN);
        createBookDto.setPublicationYear(VALID_BOOK_PUBLICATION_YEAR);
        createBookDto.setGenre(VALID_BOOK_GENRE_POETRY);
    }

    @Test
    @DisplayName("Get all books -> (valid request)")
    void getAllBooks_ValidRequest_ReturnsListOfBooks() throws Exception {
        when(bookService.findAll(any(Pageable.class))).thenReturn(
                Collections.singletonList(bookDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TITLE_0_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @DisplayName("Get book by id -> (valid request)")
    void getBookById_ValidRequest_ReturnsBookDto() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(bookDto);

        mockMvc.perform(get(BASE_URL + ID, VALID_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TITLE_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @DisplayName("Create new book -> (valid request)")
    void createBook_ValidRequest_ReturnsCreatedBookDto() throws Exception {
        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(bookDto);

        String requestContent = objectMapper.writeValueAsString(createBookDto);

        mockMvc.perform(post(BASE_URL)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(TITLE_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @DisplayName("Save a book -> (publication year in future)")
    void save_BookWithFuturePublicationYear_ReturnsValidationError() throws Exception {
        CreateBookRequestDto createBookDto = getCreateBookRequestDto();
        createBookDto.setPublicationYear(Year.now().getValue() + 1);

        String requestContent = objectMapper.writeValueAsString(createBookDto);

        mockMvc.perform(post(BASE_URL)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERRORS_EXPRESSION).value(
                       "Publication year can not be greater than the current year"));
    }

    @Test
    @DisplayName("Update book -> (valid request)")
    void updateBook_ValidRequest_ReturnsUpdatedBookDto() throws Exception {
        when(bookService.updateById(anyLong(), any(CreateBookRequestDto.class))).thenReturn(
                bookDto);

        String requestContent = objectMapper.writeValueAsString(createBookDto);

        mockMvc.perform(put(BASE_URL + ID, VALID_BOOK_ID)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestContent))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath(TITLE_EXPRESSION).value(VALID_BOOK_TITLE));
    }

    @Test
    @DisplayName("Update book -> (invalid data)")
    void updateBook_InvalidData_ReturnsBadRequest() throws Exception {
        CreateBookRequestDto invalidBookDto = new CreateBookRequestDto();
        invalidBookDto.setTitle("");
        invalidBookDto.setAuthor(VALID_BOOK_AUTHOR);
        invalidBookDto.setIsbn(VALID_BOOK_ISBN);
        invalidBookDto.setPublicationYear(VALID_BOOK_PUBLICATION_YEAR);
        invalidBookDto.setGenre(VALID_BOOK_GENRE_POETRY);

        String requestContent = objectMapper.writeValueAsString(invalidBookDto);

        mockMvc.perform(put(BASE_URL + ID, VALID_BOOK_ID)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(requestContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete book -> (valid request)")
    void deleteBook_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + ID, VALID_BOOK_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete book -> (invalid id)")
    void deleteBook_InvalidId_ReturnsNotFound() throws Exception {
        doThrow(new EntityNotFoundException(
                "Book not found with ID: " + INVALID_ID)).when(bookService).delete(anyLong());

        mockMvc.perform(delete(BASE_URL + ID, INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Search books -> (valid request)")
    void searchBooks_ValidRequest_ReturnsListOfBooks() throws Exception {
        when(bookService.search(any(BookSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(Collections.singletonList(bookDto));

        mockMvc.perform(get(SEARCH_URL)
                       .param(TITLE_PARAM_NAME, VALID_BOOK_TITLE)
                       .param(AUTHOR_PARAM_NAME, VALID_BOOK_AUTHOR)
                       .param(ISBN_PARAM_NAME, VALID_BOOK_ISBN)
                       .param(PUBLICATION_YEAR_PARAM_NAME,
                               String.valueOf(VALID_BOOK_PUBLICATION_YEAR))
                       .param(GENRE_PARAM_NAME, VALID_BOOK_GENRE_POETRY))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TITLE_0_EXPRESSION).value(VALID_BOOK_TITLE));
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
}
