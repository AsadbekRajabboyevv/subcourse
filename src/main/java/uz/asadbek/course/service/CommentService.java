package uz.asadbek.course.service;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.course.domain.Comment;
import uz.asadbek.course.domain.User;
import uz.asadbek.course.model.CommentDTO;
import uz.asadbek.course.repos.CommentRepository;
import uz.asadbek.course.repos.CourseRepository;
import uz.asadbek.course.repos.LessonRepository;
import uz.asadbek.course.repos.TestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    public static final int COURSE=1;
    public static final int LESSON=2;
    public static final int TEST=3;
    private final SessionService sessionService;
    private final LessonRepository lessonRepository;
    private final TestRepository testRepository;
    private final CourseRepository courseRepository;

    public CommentService(CommentRepository commentRepository,SessionService sessionService, LessonRepository lessonRepository, TestRepository testRepository, CourseRepository courseRepository) {
        this.commentRepository = commentRepository;
        this.sessionService = sessionService;
        this.lessonRepository = lessonRepository;
        this.testRepository = testRepository;
        this.courseRepository = courseRepository;
    }

    public List<CommentDTO> getCommentsById(Long lessonId,Long courseId,Integer userId,Long testId){
        List<Comment> result = new ArrayList<>();
        if (lessonId!=null&&courseId==null&&userId==null&&testId==null){
            result=commentRepository.findAllByLesson_Id(lessonId);
        } else if (lessonId==null&&courseId!=null&&userId==null&&testId==null) {
            result=commentRepository.findAllByCourse_Id(courseId);
        } else if (lessonId==null&&courseId==null&&userId!=null&&testId==null) {
            result=commentRepository.findAllByOwner_Id(userId);
        }
        return result.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void saveComment(String word,Long id, int type,Integer rating){
        User userFromSession = sessionService.getUser();
        Comment comment = new Comment();
        comment.setWord(word);
        comment.setRating(rating!=null?rating:0);
        comment.setUploadDate(LocalDateTime.now());
        comment.setOwner(userFromSession);
        if (type==LESSON){
            comment.setLesson(lessonRepository.findById(id).get());
        }else if (type==TEST){
            comment.setTest(testRepository.findById(id).get());

        }else if (type==COURSE){
            comment.setCourse(courseRepository.findById(id).get());
        }
        commentRepository.save(comment);
    }

    private CommentDTO mapToDTO(Comment comment){
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setWord(comment.getWord());
        dto.setRating(comment.getRating()!=null?comment.getRating():0);
        dto.setUploadDate(comment.getUploadDate());
        User owner = comment.getOwner();
        if (owner!=null){
            String fullName=owner.getFirstName()+" "+owner.getLastName();
            dto.setUserFullName(fullName);
            dto.setUserPosition(owner.getPosition().getDisplayName());
        }else{
            dto.setUserFullName("Anonim shaxs");
        }
        return dto;
    }

    public List<CommentDTO> getTop3BestComments(){
       return commentRepository.findTop3ByRatingOrderByDateCreatedDesc(5).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
