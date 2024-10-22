package shop.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.dto.request.UserRegistrationRequestDto;
import shop.dto.responce.UserResponseDto;
import shop.entity.Role;
import shop.entity.User;
import shop.exception.RegistrationException;
import shop.mapper.UserMapper;
import shop.repository.role.RoleRepository;
import shop.repository.user.UserRepository;
import shop.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registerUserRequestDto)
            throws RegistrationException {
        if (userRepository.existsUserByEmail(registerUserRequestDto.getEmail())) {
            throw new RegistrationException("Can't register user with given email");
        }
        User user = userMapper.toUser(registerUserRequestDto);
        user.setPassword(passwordEncoder.encode(registerUserRequestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRole(Role.RoleName.ROLE_USER)));
        userRepository.save(user);
        return userMapper.toRegisterUserResponseDto(user);
    }
}
