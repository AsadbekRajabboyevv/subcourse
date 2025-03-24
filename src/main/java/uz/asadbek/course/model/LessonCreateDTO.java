package uz.asadbek.course.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class LessonCreateDTO {
    @NotNull
    private String name;
    private Integer number;
    @NotNull
    private String description;
    private String videoLink;
    private List<MultipartFile> extraMaterials;

}
