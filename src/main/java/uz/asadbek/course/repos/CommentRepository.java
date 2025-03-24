package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.asadbek.course.domain.Comment;
import uz.asadbek.course.domain.Course;
import uz.asadbek.course.domain.Lesson;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findFirstByCourse(Course course);

    Comment findFirstByLesson(Lesson lesson);

    List<Comment> findAllByLesson_Id(Long lessonId);

    List<Comment> findAllByCourse_Id(Long courseId);

    List<Comment> findAllByOwner_Id(Integer ownerId);
    List<Comment> findTop3ByRatingOrderByDateCreatedDesc(int rating);

}
