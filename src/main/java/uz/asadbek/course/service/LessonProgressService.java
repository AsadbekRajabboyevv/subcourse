package uz.asadbek.course.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.course.domain.Lesson;
import uz.asadbek.course.domain.LessonProgress;
import uz.asadbek.course.domain.User;
import uz.asadbek.course.model.ProgressStatus;
import uz.asadbek.course.repos.LessonProgressRepository;
import uz.asadbek.course.repos.LessonRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class LessonProgressService {
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final SessionService sessionService;

    public LessonProgressService(LessonProgressRepository lessonProgressRepository, LessonRepository lessonRepository, SessionService sessionService) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonRepository = lessonRepository;
        this.sessionService = sessionService;
    }

    @Transactional
    public void createLessonProgressForUser(User user, Long courseId) {
        List<Lesson> lessons = lessonRepository.findAllByCourse_Id(courseId);
        for (Lesson lesson : lessons) {
            if (!lessonProgressRepository.existsByUserAndLesson(user, lesson)) {
                LessonProgress progress = new LessonProgress();
                progress.setUser(user);
                progress.setLesson(lesson);
                progress.setStatus(ProgressStatus.NOT_STARTED);
                lessonProgressRepository.save(progress);
            }
        }
    }

    public LessonProgress getLessonStatus(Long lessonId) {
        User user = sessionService.getUser();
        if (user!=null) {
            LessonProgress byLessonIdAndUserId = lessonProgressRepository.findByLesson_IdAndUser_Id(lessonId, user.getId());
            if (byLessonIdAndUserId == null) {
                return null;
            }
            return lessonProgressRepository.findByLesson_IdAndUser_Id(lessonId,user.getId());
        }
        return null;

    }
}

