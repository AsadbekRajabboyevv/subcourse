package uz.asadbek.course.repos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.asadbek.course.domain.Course;
import uz.asadbek.course.domain.User;
import uz.asadbek.course.model.CourseDTO;


public interface CourseRepository extends JpaRepository<Course, Long> {


    List<Course> findAllByUsers(User user);

    @Query("SELECT c.name FROM Course c where c.id=:id")
    String getCourseNameById(@Param("id") Long id);

    Course findByName(String name);

    @Query("SELECT c.id FROM Course c JOIN c.users u WHERE u.id = :userId")
    List<Long> findAllCourseIdsByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Course c JOIN c.users u WHERE u.id = :userId")
    List<Course> findAllByUserId(@Param("userId") Integer userId);
    @Query("""
        SELECT c FROM Course c 
        LEFT JOIN c.comments cm 
        GROUP BY c 
        ORDER BY AVG(cm.rating) DESC, SIZE(c.users) DESC
    """)
    List<Course> findTop4BestCourses(Pageable pageable);

    List<Course> findAllByScience_Id(Long scienceId);
}
