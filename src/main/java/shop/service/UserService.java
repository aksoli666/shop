package shop.service;

import shop.dto.request.user.UserRegistrationRequestDto;
import shop.dto.responce.user.UserResponseDto;
import shop.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;
}
