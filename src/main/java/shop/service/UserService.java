package shop.service;

import shop.dto.request.RegisterUserRequestDto;
import shop.dto.responce.RegisterUserResponseDto;
import shop.exception.RegistrationException;

public interface UserService {
    RegisterUserResponseDto register(RegisterUserRequestDto registerUserRequestDto)
            throws RegistrationException;
}
