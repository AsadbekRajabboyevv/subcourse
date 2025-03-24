package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.asadbek.course.domain.Question;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
