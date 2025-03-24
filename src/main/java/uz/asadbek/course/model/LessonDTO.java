package uz.asadbek.course.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LessonDTO {

    private Long id;
    private Integer number;
    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    @Size(max = 255)
    private String videoLink;

    private Long course;

}
