package com.book.management.service.impl;

import com.book.management.dto.BookResponseDto;
import com.book.management.dto.BookSearchParametersDto;
import com.book.management.dto.CreateBookRequestDto;
import com.book.management.mapper.BookMapper;
import com.book.management.repository.BookRepository;
import com.book.management.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    @Override
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        return null;
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return List.of();
    }

    @Override
    public BookResponseDto findById(Long id) {
        return null;
    }

    @Override
    public BookResponseDto updateById(Long id, CreateBookRequestDto requestDto) {
        return null;
    }

    @Override
    public List<BookResponseDto> search(BookSearchParametersDto parametersDto, Pageable pageable) {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}
