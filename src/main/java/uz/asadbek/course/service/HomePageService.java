package uz.asadbek.course.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.course.model.HomePageDTO;

@Service
@Transactional(readOnly = true)
public class HomePageService {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;
    private final TestService testService;
    private final CourseGradeService courseGradeService;
    private final CommentService commentService;

    public HomePageService(CourseService courseService, LessonService lessonService, UserService userService, TestService testService, CourseGradeService courseGradeService, CommentService commentService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.testService = testService;
        this.courseGradeService = courseGradeService;
        this.commentService = commentService;
    }

    public HomePageDTO getHomePage() {
        HomePageDTO homePage = new HomePageDTO();
        homePage.setCountCourses(courseService.countAllCourse());
        homePage.setCountLessons(lessonService.countAllLessons());
        homePage.setCountStudents(userService.countAllUsers());
        homePage.setCountTests(testService.countAllTests());
        homePage.setCourseGrades(courseGradeService.getAllCourseGrades());
        homePage.setComments(commentService.getTop3BestComments());
        homePage.setCourses(courseService.getBestCoursesTop4());
        return homePage;
    }
}
