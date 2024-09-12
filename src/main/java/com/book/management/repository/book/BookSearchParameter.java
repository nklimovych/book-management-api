package com.book.management.repository.book;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookSearchParameter {
    AUTHOR("author"),
    TITLE("title"),
    GENRE("genre");

    private final String key;
}
