package shop.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import shop.config.MapperConfig;
import shop.dto.request.book.CreateBookRequestDto;
import shop.dto.request.book.UpdateBookRequestDto;
import shop.dto.responce.book.BookDto;
import shop.dto.responce.book.BookDtoWithoutCategories;
import shop.entity.Book;
import shop.entity.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toBook(CreateBookRequestDto createBookDto);

    BookDto toBookDto(Book book);

    BookDto toBookDto(CreateBookRequestDto createBookDto);

    List<BookDto> toBookDtoList(List<Book> books);

    void updateBookFromDto(UpdateBookRequestDto updateBookDto, @MappingTarget Book book);

    List<BookDtoWithoutCategories> toBookDtoWithoutCategoriesList(List<Book> books);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Category> categories = book.getCategories();
        Set<Long> categoryIds = new HashSet<>();
        for (Category category : categories) {
            categoryIds.add(category.getId());
        }
        bookDto.setCategoryIds(categoryIds);
    }
}
