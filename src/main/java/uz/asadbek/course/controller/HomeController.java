package uz.asadbek.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import uz.asadbek.course.model.RegisterRequestDTO;
import uz.asadbek.course.service.HomePageService;


@Controller
public class HomeController {

    private final HomePageService homePageService;

    public HomeController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("homePage", homePageService.getHomePage());
        return "home/index";
    }

    @GetMapping("/test")
    public String test(Model model) {
        return "test";
    }

}
