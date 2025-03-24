package uz.asadbek.course.model.test;

import java.util.List;
import lombok.Data;

@Data
public class QuestionsItem{
	private List<OptionsItem> options;
	private int correctOptionIndex;
	private boolean hasImage;
	private String image;
	private String text;
}