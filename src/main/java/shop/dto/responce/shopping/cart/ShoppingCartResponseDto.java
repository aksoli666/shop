package shop.dto.responce.shopping.cart;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import shop.dto.responce.cart.item.CartItemResponseDto;

@Getter
@Setter
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
