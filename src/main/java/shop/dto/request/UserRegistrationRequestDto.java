package shop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import shop.entity.Role;
import shop.validation.FieldMatch;

@Setter
@Getter
@FieldMatch(
        firstFieldName = "password",
        secondFieldName = "repeatPassword",
        message = "Password must match!"
)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
    private Set<Role> roles;
}
