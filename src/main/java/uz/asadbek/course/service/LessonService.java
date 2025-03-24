package uz.asadbek.course.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.course.domain.*;
import uz.asadbek.course.model.*;
import uz.asadbek.course.repos.*;
import uz.asadbek.course.util.NotFoundException;
import uz.asadbek.course.util.ReferencedWarning;

import static uz.asadbek.course.service.CourseService.calculateAverageRating;


@Service
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final CommentRepository commentRepository;
    private final TestRepository testRepository;
    private final CommentService commentService;
    private static final String LESSON_EXTRA_FILES_FOLDER_PATH = "/uploads/lesson/";
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final CourseService courseService;
    private final LessonProgressRepository lessonProgressRepository;
    private final SessionService sessionService;
    private final LessonProgressService lessonProgressService;

    public LessonService(final LessonRepository lessonRepository,
                         final CourseRepository courseRepository, final CommentRepository commentRepository,
                         final TestRepository testRepository, CommentService commentService, QuestionRepository questionRepository, OptionRepository optionRepository, CourseService courseService, LessonProgressRepository lessonProgressRepository, SessionService sessionService, LessonProgressService lessonProgressService) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.commentRepository = commentRepository;
        this.testRepository = testRepository;
        this.commentService = commentService;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.courseService = courseService;
        this.lessonProgressRepository = lessonProgressRepository;
        this.sessionService = sessionService;
        this.lessonProgressService = lessonProgressService;
    }

    public List<LessonDTO> findAll() {
        final List<Lesson> lessons = lessonRepository.findAll(Sort.by("id"));
        return lessons.stream()
                .map(lesson -> mapToDTO(lesson, new LessonDTO()))
                .toList();
    }

    public LessonDTO get(final Long id) {
        return lessonRepository.findById(id)
                .map(lesson -> mapToDTO(lesson, new LessonDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final LessonDTO lessonDTO) {
        final Lesson lesson = new Lesson();
        mapToEntity(lessonDTO, lesson);
        return lessonRepository.save(lesson).getId();
    }

    @Transactional
    public void update(final Long id, final LessonDTO lessonDTO) {
        final Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(lessonDTO, lesson);
        lessonRepository.save(lesson);
    }

    @Transactional
    public void delete(final Long id) {
        lessonRepository.deleteById(id);
    }

    private LessonDTO mapToDTO(final Lesson lesson, final LessonDTO lessonDTO) {
        lessonDTO.setId(lesson.getId());
        lessonDTO.setName(lesson.getName());
        lessonDTO.setDescription(lesson.getDescription());
        lessonDTO.setVideoLink(lesson.getVideoLink());
        lessonDTO.setCourse(lesson.getCourse() == null ? null : lesson.getCourse().getId());
        return lessonDTO;
    }

    private Lesson mapToEntity(final LessonDTO lessonDTO, final Lesson lesson) {
        lesson.setName(lessonDTO.getName());
        lesson.setNumber(lessonDTO.getNumber());
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setVideoLink(lessonDTO.getVideoLink());
        final Course course = lessonDTO.getCourse() == null ? null : courseRepository.findById(lessonDTO.getCourse())
                .orElseThrow(() -> new NotFoundException("course not found"));
        lesson.setCourse(course);
        return lesson;
    }


    @Transactional
    public Lesson saveLessonByCourseId(LessonCreateDTO dto, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        Lesson lesson = new Lesson();
        lesson.setName(dto.getName());
        lesson.setNumber(dto.getNumber());
        lesson.setDescription(dto.getDescription());
        lesson.setVideoLink(dto.getVideoLink());
        lesson.setCourse(course);
        List<LessonFile> lessonFiles =new ArrayList<>();
        for (MultipartFile extraMaterial : dto.getExtraMaterials()) {
            LessonFile file = new LessonFile();
            file.setLesson(lesson);
            file.setFileName(extraMaterial.getOriginalFilename());
            file.setFileType(extraMaterial.getContentType());
            file.setFilePath(saveExtraFile(extraMaterial));
            saveExtraFile(extraMaterial);
            lessonFiles.add(file);
        }
        lesson.setExtraMaterials(lessonFiles);
       return lessonRepository.save(lesson);
    }


    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Comment lessonComment = commentRepository.findFirstByLesson(lesson);
        if (lessonComment != null) {
            referencedWarning.setKey("lesson.comment.lesson.referenced");
            referencedWarning.addParam(lessonComment.getId());
            return referencedWarning;
        }
        final Test lessonTest = testRepository.findFirstByLesson(lesson);
        if (lessonTest != null) {
            referencedWarning.setKey("lesson.test.lesson.referenced");
            referencedWarning.addParam(lessonTest.getId());
            return referencedWarning;
        }
        return null;
    }

    public Long countAllLessons() {
        return lessonRepository.count();
    }

    public List<LessonInfoDTO> getAllCourseId(Long courseId) {
        return lessonRepository.findAllByCourse_Id(courseId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private LessonInfoDTO mapToDTO(final Lesson lesson) {
        List<CommentDTO> comments = commentService.getCommentsById(lesson.getId(), null, null, null);
        LessonInfoDTO dto = new LessonInfoDTO();
        dto.setComments(comments);
        dto.setId(lesson.getId());
        dto.setDescription(lesson.getDescription());
        dto.setVideoLink(lesson.getVideoLink());
        dto.setNumber(lesson.getNumber());
        dto.setName(lesson.getName());
        dto.setProgressStatus(lessonProgressService.getLessonStatus(lesson.getId())!=null?lessonProgressService.getLessonStatus(lesson.getId()).getStatus():ProgressStatus.NOT_STARTED);
        dto.setLessonFiles(lesson.getExtraMaterials().stream().map(LessonFile::getFilePath).collect(Collectors.toList()));
        dto.setAverageRating(calculateAverageRating(lesson.getComments()));
        return dto;
    }
    private String saveExtraFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String fileName = LocalDateTime.now().toString().replace(":", "-") + file.getOriginalFilename();
            Path uploadPath = Paths.get(LESSON_EXTRA_FILES_FOLDER_PATH);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return LESSON_EXTRA_FILES_FOLDER_PATH + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Faylni saqlashda xatolik yuz berdi: " + e.getMessage());
        }
    }

    public LessonInfoDTO findById(Long lessonId) {
        return mapToDTO(lessonRepository.findById(lessonId).orElseThrow(NotFoundException::new));
    }
    public Lesson findByIdAsEntity(Long lessonId){
        return lessonRepository.findById(lessonId).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void startLesson(Long lessonId){
        sessionService.getUser();
        LessonProgress lessonProgress=lessonProgressRepository.findByLesson_IdAndUser_Id(lessonId, sessionService.getUser().getId());
        lessonProgress.setStatus(ProgressStatus.IN_PROGRESS);
        lessonProgress.setStartTime(LocalDateTime.now());
        lessonProgressRepository.save(lessonProgress);
    }
    @Transactional
    public void endLesson(Long lessonId){
        sessionService.getUser();
        LessonProgress lessonProgress=lessonProgressRepository.findByLesson_IdAndUser_Id(lessonId, sessionService.getUser().getId());
        lessonProgress.setStatus(ProgressStatus.COMPLETED);
        lessonProgress.setEndTime(LocalDateTime.now());
        lessonProgressRepository.save(lessonProgress);
    }

}
