package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.asadbek.course.domain.CourseGrade;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade,Long> {
}
