package uz.asadbek.course.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateDTO {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
    @NotNull
    private Long scienceId;
    @NotNull
    private Long courseGradeId;
    @NotNull
    private int duration;
    @NotNull
    @Enumerated(EnumType.STRING)
    private DurationType durationType;
    @NotEmpty
    @NotBlank
    @NotNull
    private String description;
    private MultipartFile imagePath;
    private boolean hasCertificate;
    private boolean hasVideos;
    private boolean hasTests;
    private boolean hasExtraMaterials;
    private Integer price;
}
