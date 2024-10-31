package shop.dto.request.shopping.cart;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuantityBookRequestDto {
    @Positive
    private int quantity;
}
