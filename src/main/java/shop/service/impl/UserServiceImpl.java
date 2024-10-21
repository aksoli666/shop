package shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registerUserRequestDto)
            throws RegistrationException {
        if (userRepository.existsUserByEmail(registerUserRequestDto.getEmail())) {
            throw new RegistrationException("Can't register user with given email");
        }
        User user = userMapper.toUser(registerUserRequestDto);
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));
        userRepository.save(user);
        return userMapper.toRegisterUserResponseDto(user);
    }
}
