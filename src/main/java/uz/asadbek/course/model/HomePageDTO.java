package uz.asadbek.course.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomePageDTO {
    private Long countCourses;
    private Long countStudents;
    private Long countLessons;
    private Long countTests;
    private List<CourseGradeDTO> courseGrades;
    private List<CourseDTO> courses;
    private List<CommentDTO> comments;
}
