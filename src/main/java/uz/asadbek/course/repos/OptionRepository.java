package uz.asadbek.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.asadbek.course.domain.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
