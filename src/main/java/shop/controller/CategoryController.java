package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.category.CreateCategoryRequestDto;
import shop.dto.request.category.UpdateCategoryRequestDto;
import shop.dto.responce.category.CategoryDto;
import shop.service.CategoryService;

@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "Create a new category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @Operation(
            summary = "Get all categories",
            description = "Retrieve category catalog"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(
            summary = "Get a category by id",
            description = "Retrieve a category details by id"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Update a category by id",
            description = "Update a specific category"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable @Positive Long id,
                                      @RequestBody @Valid UpdateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @Operation(
            summary = "Delete a category by id",
            description = "Delete a specific category"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteById(id);
    }
}
