package uz.asadbek.course.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.course.domain.CourseGrade;
import uz.asadbek.course.model.CourseGradeDTO;
import uz.asadbek.course.repos.CourseGradeRepository;
import uz.asadbek.course.util.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CourseGradeService {

    private final CourseGradeRepository courseGradeRepository;

    public CourseGradeService(CourseGradeRepository courseGradeRepository) {
        this.courseGradeRepository = courseGradeRepository;
    }

    public List<CourseGradeDTO> getAllCourseGrades() {
        return courseGradeRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CourseGrade findById(Long id) {
        return courseGradeRepository.findById(id).orElseThrow(()->new NotFoundException("Course Grade not found"));
    }
    private CourseGradeDTO mapToDTO(CourseGrade courseGrade) {
        CourseGradeDTO dto = new CourseGradeDTO();
        dto.setId(courseGrade.getId());
        dto.setName(courseGrade.getName());
        return dto;
    }
}
