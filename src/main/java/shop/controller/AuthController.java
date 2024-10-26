package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.user.UserLoginRequestDto;
import shop.dto.request.user.UserRegistrationRequestDto;
import shop.dto.responce.user.UserLoginResponseDto;
import shop.dto.responce.user.UserResponseDto;
import shop.exception.RegistrationException;
import shop.security.AuthenticationService;
import shop.service.UserService;

@Tag(name = "Authentication", description = "Endpoints for managing users authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Registration",
            description = "New user registration"
    )
    @PostMapping("/registration")
    public UserResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }

    @Operation(
            summary = "Login",
            description = "Login for already registered user"
    )
    @PostMapping("/login")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }
}
