package shop.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import shop.config.MapperConfig;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toShoppingCartResponseDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartResponseDto responseDto,
                           ShoppingCart shoppingCart) {
        responseDto.setUserId(shoppingCart.getUser().getId());
    }
}


