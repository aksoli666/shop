package shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dto.request.RegisterUserRequestDto;
import shop.dto.responce.RegisterUserResponseDto;
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
    public RegisterUserResponseDto register(RegisterUserRequestDto registerUserRequestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(registerUserRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user with given email");
        }
        User user = userMapper.toUser(registerUserRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toRegisterUserResponseDto(savedUser);
    }
}
