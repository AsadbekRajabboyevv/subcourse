package uz.asadbek.course.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String role;

    @Size(max = 255)
    private String description;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    private UserPositions position;

}
