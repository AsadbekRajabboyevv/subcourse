package uz.asadbek.course.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfoDTO {
    private Long id;
    private String name;
    private String description;
    private String scienceType;
    private String gradeType;
    private String duration;
    private String ownerFullName;
    private Boolean hasCertificate;
    private Boolean hasVideos;
    private Boolean hasTests;
    private Boolean hasExtraMaterials;
    private List<LessonInfoDTO> lessons;
    private List<CommentDTO> comments;
    private Long countStudents;
    private Double averageRating;

}
