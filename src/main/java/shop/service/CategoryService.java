package shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dto.request.category.CreateCategoryRequestDto;
import shop.dto.request.category.UpdateCategoryRequestDto;
import shop.dto.responce.category.CategoryDto;

public interface CategoryService {
    CategoryDto getById(Long id);

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto save(CreateCategoryRequestDto createCategoryRequestDto);

    CategoryDto update(Long id, UpdateCategoryRequestDto updateCategoryRequestDto);

    void deleteById(Long id);
}
