package uz.asadbek.course.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionDTO {
    private String text;
    private MultipartFile image;
    private List<OptionDTO> options;
}