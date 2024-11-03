package shop.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shop.config.MapperConfig;
import shop.dto.responce.order.OrderItemDto;
import shop.entity.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    Set<OrderItemDto> toOrderItemDtos(Set<OrderItem> orderItems);
}
