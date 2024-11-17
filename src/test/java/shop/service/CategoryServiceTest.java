package shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static shop.util.TestUtil.createCategory;
import static shop.util.TestUtil.createCategoryDto;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.dto.request.category.CreateCategoryRequestDto;
import shop.dto.request.category.UpdateCategoryRequestDto;
import shop.dto.responce.category.CategoryDto;
import shop.entity.Category;
import shop.exception.EntityNotFoundException;
import shop.mapper.CategoryMapper;
import shop.repository.CategoryRepository;
import shop.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 20;
    private static final Long ID_1L_CORRECT = 1L;
    private static final Long INCORRECT_ID = -100L;
    private static final String CATEGORY_NAME_CORRECT = "Test Category";
    private static final String CATEGORY_DESCRIPTION_CORRECT = "Test Description";

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("""
            Verify save(...), return CategoryDto
            """)
    public void save_ValidCreateCategoryDto_returnCategoryDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName(CATEGORY_NAME_CORRECT);
        dto.setDescription(CATEGORY_DESCRIPTION_CORRECT);

        Category category = createCategory();

        CategoryDto expected = createCategoryDto();

        when(categoryMapper.toCategory(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.save(dto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify getById(...), return CategoryDto
            """)
    public void getById_ValidId_returnCategoryDto() {
        Category category = createCategory();

        CategoryDto expected = createCategoryDto();

        when(categoryRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.getById(ID_1L_CORRECT);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify getById(...), throw EntityNotFoundEntityException
            """)
    public void getById_InvalidId_throwEntityNotFoundEntityException() {
        when(categoryRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(INCORRECT_ID));
    }

    @Test
    @DisplayName("""
            Verify findAll(...), return Page<CategoryDto>
            """)
    public void findAll_ValidPage_returnPageCategoryDto() {
        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Category category = createCategory();

        CategoryDto dto = createCategoryDto();

        Page<Category> categories = new PageImpl<>(List.of(category), pageable, 1);
        Page<CategoryDto> expected = new PageImpl<>(List.of(dto), pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categories);
        when(categoryMapper.toCategoryDtoPage(categories)).thenReturn(expected);

        Page<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify update(...), return CategoryDto
            """)
    public void update_ValidId_returnCategoryDto() {
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto();
        dto.setName(CATEGORY_NAME_CORRECT);
        dto.setDescription(CATEGORY_DESCRIPTION_CORRECT);

        Category category = createCategory();

        CategoryDto expected = createCategoryDto();

        when(categoryRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.update(ID_1L_CORRECT, dto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify update(...), throw EntityNotFoundException
            """)
    public void update_InvalidId_throwEntityNotFoundException() {
        when(categoryRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(INCORRECT_ID, new UpdateCategoryRequestDto()));
    }
}
