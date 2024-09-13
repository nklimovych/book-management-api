package com.book.management.repository.book;

import static com.book.management.util.TestConstants.AUTHOR_PARAM_NAME;
import static com.book.management.util.TestConstants.GENRE_PARAM_NAME;
import static com.book.management.util.TestConstants.INVALID_PARAM_NAME;
import static com.book.management.util.TestConstants.TITLE_PARAM_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.book.management.model.Book;
import com.book.management.repository.SpecificationProvider;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("Book Specification Provider Manager Tests")
class BookSpecificationProviderManagerTest {

    @Mock
    private SpecificationProvider<Book> titleSpecificationProvider;

    @Mock
    private SpecificationProvider<Book> authorSpecificationProvider;

    @Mock
    private SpecificationProvider<Book> genreSpecificationProvider;

    @InjectMocks
    private BookSpecificationProviderManager bookSpecificationProviderManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(titleSpecificationProvider.getKey()).thenReturn(TITLE_PARAM_NAME);
        when(authorSpecificationProvider.getKey()).thenReturn(AUTHOR_PARAM_NAME);
        when(genreSpecificationProvider.getKey()).thenReturn(GENRE_PARAM_NAME);

        bookSpecificationProviderManager = new BookSpecificationProviderManager(
                List.of(
                        titleSpecificationProvider,
                        authorSpecificationProvider,
                        genreSpecificationProvider
                )
        );
    }

    @Test
    @DisplayName("Get specification provider -> (valid key)")
    void getSpecificationProvider_WithValidKey_ReturnsCorrectProvider() {
        SpecificationProvider<Book> result =
                bookSpecificationProviderManager.getSpecificationProvider(TITLE_PARAM_NAME);
        assertEquals(titleSpecificationProvider, result);

        result = bookSpecificationProviderManager.getSpecificationProvider(AUTHOR_PARAM_NAME);
        assertEquals(authorSpecificationProvider, result);

        result = bookSpecificationProviderManager.getSpecificationProvider(GENRE_PARAM_NAME);
        assertEquals(genreSpecificationProvider, result);
    }

    @Test
    @DisplayName("Get specification provider -> (invalid key)")
    void getSpecificationProvider_WithInvalidKey_ThrowsException() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                bookSpecificationProviderManager.getSpecificationProvider(INVALID_PARAM_NAME));

        String expectedMessage = "Can't find specification provider for invalidParam";
        assertEquals(expectedMessage, exception.getMessage());
    }
}
