package uz.asadbek.course.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OptionDTO {
    private String text;
    private boolean isCorrect;
}
