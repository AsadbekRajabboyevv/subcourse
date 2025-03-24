package uz.asadbek.course.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.course.domain.Comment;
import uz.asadbek.course.domain.Course;
import uz.asadbek.course.domain.Lesson;
import uz.asadbek.course.domain.User;
import uz.asadbek.course.model.CourseCreateDTO;
import uz.asadbek.course.model.CourseDTO;
import uz.asadbek.course.model.CourseInfoDTO;
import uz.asadbek.course.repos.*;
import uz.asadbek.course.util.NotFoundException;
import uz.asadbek.course.util.ReferencedWarning;


@Service
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final CommentRepository commentRepository;
    private static final String COURSE_IMAGE_FOLDER_PATH = "/uploads/course/";
    private final ScienceRepository scienceRepository;
    private final SessionService sessionService;
    private final LessonService lessonService;
    private final CommentService commentService;
    private final CourseGradeService courseGradeService;

    public CourseService(final CourseRepository courseRepository,
                         final LessonRepository lessonRepository,
                         final CommentRepository commentRepository,
                         final ScienceRepository scienceRepository,
                         final SessionService sessionService,
                         final @Lazy LessonService lessonService,
                         final CommentService commentService,
                         final CourseGradeService courseGradeService) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.commentRepository = commentRepository;
        this.scienceRepository = scienceRepository;
        this.sessionService = sessionService;
        this.lessonService = lessonService;
        this.commentService = commentService;
        this.courseGradeService = courseGradeService;
    }

    public List<CourseDTO> findAll() {
        final List<Course> courses = courseRepository.findAll(Sort.by("id"));
        return courses.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public CourseInfoDTO getCourseInfo(String name){
        return mapToInfoDTO(courseRepository.findByName(name));
    }

    public CourseInfoDTO getCourseInfo(Long id){
        return mapToInfoDTO(courseRepository.findById(id).orElseThrow(()->new NotFoundException("Course not found")));
    }


    @Transactional
    public Long create(final CourseCreateDTO courseDTO) {
        Course course = mapToEntity(courseDTO);
        return courseRepository.save(course).getId();
    }

    @Transactional
    public void delete(final Long id) {
        courseRepository.deleteById(id);
    }

    private Course mapToEntity(final CourseCreateDTO dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setScience(scienceRepository.findById(dto.getScienceId()).orElseThrow(()->new NotFoundException("science not found")));
        course.setDescription(dto.getDescription());
        course.setHasCertificate(dto.isHasCertificate());
        course.setHasTests(dto.isHasTests());
        course.setHasVideos(dto.isHasVideos());
        course.setHasExtraMaterials(dto.isHasExtraMaterials());
        course.setImagePath(saveImage(dto.getImagePath()));
        course.setCourseGrade(courseGradeService.findById(dto.getCourseGradeId()));
        course.setDuration(dto.getDuration());
        course.setDurationType(dto.getDurationType());
        course.setPrice(dto.getPrice());
        course.setOwner(sessionService.getUser());
        return course;
    }

    private CourseDTO mapToDTO(final Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setType(course.getScience().getName());
        courseDTO.setCountStudents((long) course.getUsers().size());
        courseDTO.setCountLessons((long) course.getLessons().size());
        courseDTO.setOwnerFullName(course.getOwner().getFirstName()+" "+course.getOwner().getLastName());
        courseDTO.setCountVideoLessons((long) course.getLessons().size());
        courseDTO.setImagePath(course.getImagePath());
        int totalRatings = course.getComments() != null
                ? course.getComments()
                .stream()
                .map(Comment::getRating)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum()
                : 0;

        courseDTO.setRatings(totalRatings);

        courseDTO.setAverageRating(calculateAverageRating(course.getComments()));

        return courseDTO;
    }
    public static double calculateAverageRating(Set<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return 0.0;
        }

        double avg = comments.stream()
                .filter(comment -> comment != null && comment.getRating() != null)
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0.0);

        return Math.round(avg * 10.0) / 10.0;
    }

    private CourseInfoDTO mapToInfoDTO(final Course course) {
        CourseInfoDTO courseDTO = new CourseInfoDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setCountStudents((long) course.getUsers().size());
        if (course.getDuration()!=null&&course.getDurationType()!=null){
            courseDTO.setDuration(course.getDuration()+" "+course.getDurationType().name());
        }
        courseDTO.setHasCertificate(course.isHasCertificate());
        courseDTO.setHasTests(course.isHasTests());
        courseDTO.setHasVideos(course.isHasVideos());
        courseDTO.setHasExtraMaterials(course.isHasExtraMaterials());
        courseDTO.setGradeType(course.getCourseGrade().getName());
        courseDTO.setScienceType(course.getScience().getName());
        courseDTO.setOwnerFullName(course.getOwner().getFirstName()+" "+course.getOwner().getLastName());
        courseDTO.setLessons(lessonService.getAllCourseId(course.getId()));
        courseDTO.setComments(commentService.getCommentsById(null,course.getId(),null,null));
        courseDTO.setAverageRating(calculateAverageRating(course.getComments()));

        return courseDTO;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Course course = courseRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Lesson courseLesson = lessonRepository.findFirstByCourse(course);
        if (courseLesson != null) {
            referencedWarning.setKey("course.lesson.course.referenced");
            referencedWarning.addParam(courseLesson.getId());
            return referencedWarning;
        }
        final Comment courseComment = commentRepository.findFirstByCourse(course);
        if (courseComment != null) {
            referencedWarning.setKey("course.comment.course.referenced");
            referencedWarning.addParam(courseComment.getId());
            return referencedWarning;
        }
        return null;
    }
    public Long countAllCourse(){
        return courseRepository.count();
    }
    private String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(COURSE_IMAGE_FOLDER_PATH);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return COURSE_IMAGE_FOLDER_PATH + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Faylni saqlashda xatolik yuz berdi: " + e.getMessage());
        }
    }

   public List<Long> currentUserCourseIds(){
       User user = sessionService.getUser();
       if (user!=null){
           return courseRepository.findAllCourseIdsByUserId(user.getId());
       }else{
           return new ArrayList<Long>();
       }
    }

    public List<CourseDTO> myCourses() {
        User user = sessionService.getUser();
        if (user!=null){
            return courseRepository.findAllByUserId(user.getId()).stream().map(this::mapToDTO).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    public List<CourseDTO> getBestCoursesTop4() {
        return courseRepository.findTop4BestCourses(PageRequest.of(0, 4)).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<CourseDTO> findAllByScienceId(Long id) {
        return courseRepository.findAllByScience_Id(id).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Course findById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(()->new NotFoundException("Course not found"));
    }
}
