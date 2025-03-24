package uz.asadbek.course.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.asadbek.course.model.LessonInfoDTO;
import uz.asadbek.course.model.TestDTO;
import uz.asadbek.course.repos.CourseRepository;
import uz.asadbek.course.service.CommentService;
import uz.asadbek.course.service.LessonService;


@Controller
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final CommentService commentService;

    public LessonController(final LessonService lessonService,
                            final CommentService commentService) {
        this.lessonService = lessonService;
        this.commentService = commentService;
    }


    @GetMapping("/view/{lessonId}")
    public String getLessonByCourseId(@PathVariable("lessonId")Long lessonId, final Model model) {
        LessonInfoDTO lessonInfoDTO=lessonService.findById(lessonId);
        model.addAttribute("lesson", lessonInfoDTO);
        return "lesson/view";
    }
    @PostMapping("/comment/add/{id}")
    public String addComment(@PathVariable Long id, @RequestParam String content, @RequestParam("rating")Integer rating) {
        commentService.saveComment(content,id, CommentService.LESSON,rating);
        return "redirect:/lessons/view/" + id;
    }
    @GetMapping("/add-test/{lessonId}")
    public String addTest(@PathVariable Long lessonId, Model model) {
        model.addAttribute("lessonId", lessonId);
        return "lesson/add-test";
    }
    @PostMapping("/add-test/{lessonId}")
    public String testAdd(@PathVariable("lessonId")Long lessonId,@ModelAttribute("test")@Valid final TestDTO testDTO, Model model) {
        return "redirect:/lessons/view/" + lessonId;
    }

    @PostMapping("/start-lesson/{id}")
    public String startLesson(@PathVariable("id")Long id){
        lessonService.startLesson(id);
        return "redirect:/lessons/view/" + id;
    }
    @PostMapping("/end-lesson/{id}")
    public String endLesson(@PathVariable("id")Long id){
        lessonService.endLesson(id);
        return "redirect:/lessons/view/" + id;
    }
}
