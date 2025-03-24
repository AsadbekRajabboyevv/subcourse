package uz.asadbek.course.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.asadbek.course.domain.Lesson;
import uz.asadbek.course.domain.User;
import uz.asadbek.course.model.*;
import uz.asadbek.course.repos.UserRepository;
import uz.asadbek.course.service.*;
import uz.asadbek.course.util.CustomCollectors;
import uz.asadbek.course.util.ReferencedWarning;
import uz.asadbek.course.util.WebUtils;

import java.util.List;


@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserRepository userRepository;
    private final CourseGradeService courseGradeService;
    private final ScienceService scienceService;
    private final CommentService commentService;
    private final SessionService sessionService;
    private final UserService userService;
    private final LessonService lessonService;
    private final LessonProgressService lessonProgressService;

    public CourseController(final CourseService courseService,
                            final UserRepository userRepository, CourseGradeService courseGradeService, ScienceService scienceService, CommentService commentService, SessionService sessionService, UserService userService, LessonService lessonService, LessonProgressService lessonProgressService) {
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.courseGradeService = courseGradeService;
        this.scienceService = scienceService;
        this.commentService = commentService;
        this.sessionService = sessionService;
        this.userService = userService;
        this.lessonService = lessonService;
        this.lessonProgressService = lessonProgressService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("usersValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getEmail)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("title","Barcha Kurslar");
        return "course/list";
    }

    @GetMapping("/add")
    public String add(Model model,@ModelAttribute("course") final CourseCreateDTO courseCreateDTO) {
        model.addAttribute("courseGrades",courseGradeService.getAllCourseGrades());
        model.addAttribute("scienceTypes",scienceService.getAllSciences());
        return "course/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("course") @Valid final CourseCreateDTO courseDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            for (FieldError allError : allErrors) {
                System.out.println(allError.getField()+":"+allError.getDefaultMessage());
            }
            return "course/add";
        }
        courseService.create(courseDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("course.create.success"));
        return "redirect:/courses";
    }

    @GetMapping("/view/science/{id}")
    public String getCoursesScienceId(@RequestParam("scienceName")String scienceName,@PathVariable("id")Long id,final Model model) {
       List<CourseDTO>courses= courseService.findAllByScienceId(id);
        model.addAttribute("courses",courses);
        model.addAttribute("title",scienceName);
        return "course/list";
    }
    @GetMapping("/view/{id}")
    public String getCourseByName(@PathVariable Long id, Model model) {
        CourseInfoDTO course = courseService.getCourseInfo(id);
        model.addAttribute("course", course);
        model.addAttribute("userCoursesIds",courseService.currentUserCourseIds());
        return "course/view";
    }

    @GetMapping("/view/me")
    public String myCourses(Model model) {
        List<CourseDTO> myCourses=courseService.myCourses();
        model.addAttribute("courses", myCourses);
        model.addAttribute("title","Mening kurslarim");
        return "course/list";
    }

    @PostMapping("/comment/add/{id}")
    public String addComment(@PathVariable Long id, @RequestParam String content,@RequestParam("rating")Integer rating) {
        commentService.saveComment(content,id,CommentService.COURSE,rating);
        return "redirect:/courses/view/" + id;
    }

    @GetMapping("/add-lesson/{id}")
    public String addLesson(@PathVariable Long id, Model model) {
        model.addAttribute("courseId",id);
        model.addAttribute("lesson",new LessonCreateDTO());
        return "lesson/add";
    }

    @PostMapping("/add-lesson/{courseId}")
    public String postLesson(@ModelAttribute("lesson")@Valid final LessonCreateDTO dto,@PathVariable Long courseId, Model model){
        Lesson createdLesson = lessonService.saveLessonByCourseId(dto, courseId);
        model.addAttribute("lesson",createdLesson);
        return "redirect:/lessons/view/"+createdLesson.getId();
    }



    @PostMapping("/enroll/{id}")
    public String enroll(@PathVariable("id") Long id,RedirectAttributes redirectAttributes) {
        User user = sessionService.getUser();
        if (user!=null){
            userService.enrollUserToCourse(user.getId(),id);
            lessonProgressService.createLessonProgressForUser(user,id);
            redirectAttributes.addFlashAttribute("message", "enroll.course.message");
        }
        redirectAttributes.addFlashAttribute("messageError", "enroll.course.error.message");
        return "redirect:/";
    }


}
