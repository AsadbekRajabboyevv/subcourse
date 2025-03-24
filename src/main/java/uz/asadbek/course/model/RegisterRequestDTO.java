package uz.asadbek.course.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private LocalDate birthDate;

    @NotNull
    private String phone;

    private UserPositions position;
}
