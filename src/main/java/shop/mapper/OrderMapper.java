package shop.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import shop.config.MapperConfig;
import shop.dto.responce.order.OrderResponseDto;
import shop.entity.Order;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    OrderResponseDto toOrderResponseDto(Order order);

    @AfterMapping
    default void setUserId(@MappingTarget OrderResponseDto orderResponseDto,
                           Order order) {
        orderResponseDto.setUserId(order.getUser().getId());
    }
}
