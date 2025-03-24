package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.asadbek.course.domain.Course;
import uz.asadbek.course.domain.Lesson;

import java.util.List;


public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Lesson findFirstByCourse(Course course);

    @Query("SELECT l FROM Lesson l where l.course.id=:courseId order by l.number asc")
    List<Lesson> findAllByCourse_Id(@Param("courseId")Long courseId);
}
