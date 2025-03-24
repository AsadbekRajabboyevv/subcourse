package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.asadbek.course.domain.Science;


public interface ScienceRepository extends JpaRepository<Science, Long> {
}
