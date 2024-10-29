package shop.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import shop.config.MapperConfig;
import shop.dto.request.category.CreateCategoryRequestDto;
import shop.dto.request.category.UpdateCategoryRequestDto;
import shop.dto.responce.category.CategoryDto;
import shop.entity.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toCategory(CreateCategoryRequestDto requestDto);

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryPage);

    void updateCategory(UpdateCategoryRequestDto requestDto, @MappingTarget Category category);

    default Page<CategoryDto> toCategoryDtoPage(Page<Category> categoryPage) {
        List<CategoryDto> dtos = toCategoryDtoList(categoryPage.getContent());
        return new PageImpl<>(dtos, categoryPage.getPageable(), categoryPage.getTotalElements());
    }
}
