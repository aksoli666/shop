package shop.mapper;

import org.mapstruct.Mapper;
import shop.config.MapperConfig;
import shop.dto.request.RegisterUserRequestDto;
import shop.dto.responce.RegisterUserResponseDto;
import shop.entity.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(RegisterUserRequestDto registerUserRequestDto);

    RegisterUserResponseDto toRegisterUserResponseDto(User user);
}
