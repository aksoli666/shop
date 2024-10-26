package shop.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.dto.request.category.CreateCategoryRequestDto;
import shop.dto.request.category.UpdateCategoryRequestDto;
import shop.dto.responce.category.CategoryDto;
import shop.entity.Category;
import shop.exception.EntityNotFoundException;
import shop.mapper.CategoryMapper;
import shop.repository.CategoryRepository;
import shop.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get category by id: " + id));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryMapper.toCategoryDtoList(
                categoryRepository.findAll(pageable).getContent());
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto createCategoryRequestDto) {
        Category category = categoryRepository.save(
                categoryMapper.toCategory(createCategoryRequestDto));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto update(Long id, UpdateCategoryRequestDto updateCategoryRequestDto) {
        Category category = categoryMapper.toCategory(getById(id));
        categoryMapper.updateCategory(updateCategoryRequestDto, category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
