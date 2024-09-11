package com.book.management.mapper;

import com.book.management.config.MapperConfig;
import com.book.management.dto.BookResponseDto;
import com.book.management.dto.CreateBookRequestDto;
import com.book.management.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookResponseDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toEntity(CreateBookRequestDto requestDto);
}
