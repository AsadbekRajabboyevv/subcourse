package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.asadbek.course.domain.Lesson;
import uz.asadbek.course.domain.LessonProgress;
import uz.asadbek.course.domain.User;

import java.util.List;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    boolean existsByUserAndLesson(User user, Lesson lesson);

    List<LessonProgress> findByLesson_Id(Long lessonId);

    LessonProgress findByLesson_IdAndUser_Id(Long lessonId, Integer userId);
}
