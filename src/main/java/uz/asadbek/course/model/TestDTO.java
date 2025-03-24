package uz.asadbek.course.model;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestDTO {
    private String title;
    private List<QuestionDTO> questions;
}