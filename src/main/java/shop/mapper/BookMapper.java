package shop.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import shop.config.MapperConfig;
import shop.dto.request.CreateBookDto;
import shop.dto.responce.BookDto;
import shop.entity.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toBook(CreateBookDto createBookDto);

    BookDto toBookDto(Book book);

    List<BookDto> toBookDtoList(List<Book> books);
}
