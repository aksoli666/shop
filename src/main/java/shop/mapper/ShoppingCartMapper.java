package shop.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import shop.config.MapperConfig;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.ShoppingCart;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toShoppingCartResponseDto(ShoppingCart shoppingCart);

    ShoppingCart toShoppingCart(ShoppingCartResponseDto shoppingCartResponseDto);

    void updateShoppingCart(ShoppingCart shoppingCart, @MappingTarget ShoppingCart shoppingCar);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartResponseDto responseDto,
                           ShoppingCart shoppingCart) {
        responseDto.setUserId(shoppingCart.getUser().getId());
    }
}
