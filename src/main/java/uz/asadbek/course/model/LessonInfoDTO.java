package uz.asadbek.course.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.asadbek.course.domain.LessonFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonInfoDTO {
    private Long id;
    private String name;
    private String description;
    private String videoLink;
    private Integer number;
    private List<CommentDTO>comments;
    private Double averageRating;
    private List<String> lessonFiles;
    private ProgressStatus progressStatus;
    private LocalDateTime progressDateTime;
}
