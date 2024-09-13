package com.book.management.util;

public class TestConstants {
    public static final String TEST_DB = "classpath:database/test-db.sql";

    public static final Long VALID_BOOK_ID = 1L;
    public static final String VALID_BOOK_TITLE = "Kobzar";
    public static final String VALID_TITLE_IN_DIFFERENT_CASES = "kObZaR";
    public static final String VALID_BOOK_AUTHOR = "Taras Shevchenko";
    public static final String VALID_AUTHOR_IN_DIFFERENT_CASES = "tArAs sHeVcHenKo";
    public static final Integer VALID_BOOK_PUBLICATION_YEAR = 1840;
    public static final String VALID_BOOK_GENRE_POETRY = "Poetry";
    public static final String VALID_GENRE_IN_DIFFERENT_CASES_POETRY = "pOetRy";
    public static final String VALID_BOOK_ISBN = "978-1-1516-4732-0";
    public static final String ISBN_DUPLICATE = "978-7-3664-5711-2";
    public static final String ISBN_DIFFERENT = "978-7-3664-5711-3";

    public static final String VALID_BOOK_AUTHOR_NOT_IN_DB = "Lina Kostenko";
    public static final String VALID_BOOK_ISBN_NOT_IN_DB = "978-1-2345-6789-0";

    public static final Long INVALID_ID = -1L;
    public static final String INVALID_BOOK_ISBN = "978-11-1516-4732-02";
    public static final String INVALID_PARAM_NAME = "invalidParam";

    public static final String TITLE_PARAM_NAME = "title";
    public static final String AUTHOR_PARAM_NAME = "author";
    public static final String ISBN_PARAM_NAME = "isbn";
    public static final String PUBLICATION_YEAR_PARAM_NAME = "publicationYear";
    public static final String GENRE_PARAM_NAME = "genre";

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 5;
    public static final int ZERO_PAGE_SIZE = 0;
    public static final int NEGATIVE_PAGE_SIZE = -1;
    public static final int NUMBER_OF_INVOCATIONS = 1;
    public static final int EXPECTED_LIST_SIZE = 1;
    public static final String EMPTY_STRING = "";

    public static final String ID = "/{id}";
    public static final String BASE_URL = "/books";
    public static final String SEARCH_URL = BASE_URL + "/search";

    public static final String TITLE_0_EXPRESSION = "$[0].title";
    public static final String TITLE_EXPRESSION = "$.title";
    public static final String ERRORS_EXPRESSION = "$.errors[0]";

    public static final String VALID_AUTHOR_SEARCH_PREDICATE = "%taras shevchenko%";
    public static final String VALID_TITLE_SEARCH_PREDICATE = "%kobzar%";
    public static final String VALID_GENRE_SEARCH_PREDICATE = "%poetry%";
}
