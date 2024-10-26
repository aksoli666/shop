package shop.dto.request.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequestDto {
    private String name;
    private String description;
}
