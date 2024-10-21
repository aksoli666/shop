package shop.mapper;

import org.mapstruct.Mapper;
import shop.config.MapperConfig;
import shop.dto.request.UserRegistrationRequestDto;
import shop.dto.responce.UserResponseDto;
import shop.entity.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegistrationRequestDto userRegistrationRequestDto);

    UserResponseDto toRegisterUserResponseDto(User user);
}
