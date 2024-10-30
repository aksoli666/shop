package shop.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import shop.config.MapperConfig;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.entity.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);

    Set<CartItemResponseDto> toCartItemResponseDtos(Set<CartItem> cartItems);

    void updateCartItem(UpdateQuantityBookRequestDto requestDto, @MappingTarget CartItem cartItem);

    void updateCartItem(CartItem source, @MappingTarget CartItem cartItem);
}
