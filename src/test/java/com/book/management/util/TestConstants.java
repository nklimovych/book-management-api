package com.book.management.util;

public class TestConstants {
    public static final String TEST_DB = "classpath:database/test-db.sql";

    public static final Long VALID_BOOK_ID = 1L;
    public static final String VALID_BOOK_TITLE = "Kobzar";
    public static final String VALID_BOOK_AUTHOR = "Taras Shevchenko";
    public static final Integer VALID_BOOK_PUBLICATION_YEAR = 1840;
    public static final String VALID_BOOK_GENRE_POETRY = "Poetry";
    public static final String VALID_GENRE_IN_DIFFERENT_CASES_POETRY = "pOetRy";
    public static final String VALID_BOOK_ISBN = "978-1-1516-4732-0";

    public static final String VALID_BOOK_AUTHOR_NOT_IN_DB = "Lina Kostenko";
    public static final String VALID_BOOK_ISBN_NOT_IN_DB = "978-1-2345-6789-0";

    public static final Long INVALID_ID = -1L;
    public static final String INVALID_BOOK_ISBN = "978-11-1516-4732-02";

    public static final String TITLE_PARAM_NAME = "title";
    public static final String AUTHOR_PARAM_NAME = "author";
    public static final String ISBN_PARAM_NAME = "isbn";
    public static final String GENRE_PARAM_NAME = "genre";

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 5;
    public static final int ZERO_PAGE_SIZE = 0;
    public static final int NEGATIVE_PAGE_SIZE = -1;

    public static final String EMPTY_STRING = "";
}
