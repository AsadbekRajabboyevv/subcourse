package uz.asadbek.course.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CourseDTO {

    private Long id;
    private String name;
    private String typeIcon;
    private String type;
    private String description;
    private String imagePath;
    private String ownerFullName;
    private Long countStudents;
    private Long countLessons;
    private Long countVideoLessons;
    private Integer ratings;
    private Double averageRating;
}
