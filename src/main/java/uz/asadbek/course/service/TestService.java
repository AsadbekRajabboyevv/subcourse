package uz.asadbek.course.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.course.domain.Option;
import uz.asadbek.course.domain.Question;
import uz.asadbek.course.domain.Test;
import uz.asadbek.course.model.test.TestCreateDTO;
import uz.asadbek.course.repos.TestRepository;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TestService {

    private final TestRepository testRepository;
    private final LessonService lessonService;
    private final ScienceService scienceService;

    public TestService(TestRepository testRepository, LessonService lessonService, ScienceService scienceService) {
        this.testRepository = testRepository;
        this.lessonService = lessonService;
        this.scienceService = scienceService;
    }

    public Long countAllTests() {
        return testRepository.count();
    }

    @Transactional
    public Long createTest(TestCreateDTO testCreateDTO) {
        Test test = new Test();
        test.setTitle(testCreateDTO.getTitle());
        test.setScience(null);
        test.setDuration(testCreateDTO.getDuration());
        test.setLesson(lessonService.findByIdAsEntity(testCreateDTO.getLessonId()));
        List<Question> questions = testCreateDTO.getQuestions().stream().map(qDto -> {
            Question question = new Question();
            question.setText(qDto.getText());
            question.setHasImage(qDto.isHasImage());
            question.setCorrectOptionIndex(qDto.getCorrectOptionIndex());
            question.setTest(test);

            if (qDto.isHasImage() && qDto.getImage() != null && !qDto.getImage().isEmpty()) {
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(qDto.getImage());
                    question.setImageData(imageBytes);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid Base64 image data", e);
                }
            }
            List<Option> options = qDto.getOptions().stream().map(oDto -> {
                Option option = new Option();
                option.setText(oDto.getText());
                option.setQuestion(question);
                return option;
            }).collect(Collectors.toList());

            question.setOptions(options);
            return question;
        }).collect(Collectors.toList());

        test.setQuestions(questions);

        return testRepository.save(test).getId();
    }
}
