package uz.asadbek.course.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    @Size(min = 1, max = 50)
    private String word;
    private String userFullName;
    private String userPosition;
    private Integer rating;
    private LocalDateTime uploadDate;
}
