package com.book.management.service;

import com.book.management.dto.BookResponseDto;
import com.book.management.dto.BookSearchParametersDto;
import com.book.management.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    BookResponseDto updateById(Long id, CreateBookRequestDto requestDto);

    List<BookResponseDto> search(BookSearchParametersDto parametersDto, Pageable pageable);

    void delete(Long id);
}
