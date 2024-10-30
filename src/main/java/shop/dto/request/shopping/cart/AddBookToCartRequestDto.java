package shop.dto.request.shopping.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookToCartRequestDto {
    @NotNull
    private Long bookId;
    private int quantity;
}
