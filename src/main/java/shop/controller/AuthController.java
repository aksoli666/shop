package shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.RegisterUserRequestDto;
import shop.dto.responce.RegisterUserResponseDto;
import shop.exception.RegistrationException;
import shop.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/registration")
    public RegisterUserResponseDto register(
            @Valid @RequestBody RegisterUserRequestDto registerUserRequestDto)
            throws RegistrationException {
        return userService.register(registerUserRequestDto);
    }
}
