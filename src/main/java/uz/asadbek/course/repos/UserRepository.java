package uz.asadbek.course.repos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.asadbek.course.domain.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);

    User findByConfirmationToken(String confirmationToken);

    boolean existsByEmail(@NotNull @Email String email);
}
