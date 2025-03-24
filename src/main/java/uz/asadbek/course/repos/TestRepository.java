package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.asadbek.course.domain.Lesson;
import uz.asadbek.course.domain.Test;


public interface TestRepository extends JpaRepository<Test, Long> {

    Test findFirstByLesson(Lesson lesson);

}
