package shop.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
