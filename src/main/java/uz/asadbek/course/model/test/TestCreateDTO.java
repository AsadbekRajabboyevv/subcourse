package uz.asadbek.course.model.test;

import java.util.List;
import lombok.Data;

@Data
public class TestCreateDTO{
	private Long lessonId;
	private String title;
	private int duration;
	private List<QuestionsItem> questions;
}