package shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shop.config.MapperConfig;
import shop.dto.request.UserRegistrationRequestDto;
import shop.dto.responce.UserResponseDto;
import shop.entity.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "roles", source = "roles")
    User toUser(UserRegistrationRequestDto userRegistrationRequestDto);

    UserResponseDto toRegisterUserResponseDto(User user);
}
