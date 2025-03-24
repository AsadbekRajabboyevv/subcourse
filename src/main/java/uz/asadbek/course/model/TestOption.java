package uz.asadbek.course.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class TestOption {

    @NotNull
    @Size(max = 255)
    private String question;

    @NotNull
    private Boolean isTrue;

}
