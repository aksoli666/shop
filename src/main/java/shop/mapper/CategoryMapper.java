package shop.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
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

    CategoryDto toCategoryDto(CreateCategoryRequestDto requestDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);

    void updateCategory(UpdateCategoryRequestDto requestDto, @MappingTarget Category category);
}
