package shop.dto.responce;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import shop.entity.Role;

@Setter
@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
    private Set<Role> roles;
}
