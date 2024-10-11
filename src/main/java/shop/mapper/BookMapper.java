package shop.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import shop.config.MapperConfig;
import shop.dto.request.CreateBookRequestDto;
import shop.dto.request.UpdateBookRequestDto;
import shop.dto.responce.BookDto;
import shop.entity.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toBook(CreateBookRequestDto createBookDto);

    BookDto toBookDto(Book book);

    List<BookDto> toBookDtoList(List<Book> books);

    void updateBookFromDto(UpdateBookRequestDto updateBookDto, @MappingTarget Book book);
}
