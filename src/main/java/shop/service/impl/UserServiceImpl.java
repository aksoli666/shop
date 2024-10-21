package shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dto.request.UserRegistrationRequestDto;
import shop.dto.responce.UserResponseDto;
import shop.entity.User;
import shop.exception.RegistrationException;
import shop.mapper.UserMapper;
import shop.repository.user.UserRepository;
import shop.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registerUserRequestDto)
            throws RegistrationException {
        if (userRepository.existsUserByEmail(registerUserRequestDto.getEmail())) {
            throw new RegistrationException("Can't register user with given email");
        }
        User user = userMapper.toUser(registerUserRequestDto);
        userRepository.save(user);
        return userMapper.toRegisterUserResponseDto(user);
    }
}
