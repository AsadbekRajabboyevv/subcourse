package uz.asadbek.course.controller;

import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.asadbek.course.model.RegisterRequestDTO;
import uz.asadbek.course.service.UserService;

import java.util.Locale;

@Controller
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;

    private final MessageSource messageSource;

    public AuthController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @PostMapping("/register")
    public String register(Model model,@ModelAttribute("register") @Valid RegisterRequestDTO registerRequestDTO, RedirectAttributes redirectAttributes,BindingResult bindingResult) {
        try {
            userService.register(registerRequestDTO);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "home/index";
        }
    }

    @GetMapping("/confirm")
    public String confirmUser(@RequestParam("confirmToken")String confirmToken, RedirectAttributes redirectAttributes) {
        userService.confirmUser(confirmToken);
        return "redirect:/auth/account-success";
    }

    @GetMapping("/account-success")
    public String accountSuccess(Model model, Locale locale) {
        model.addAttribute("successMessage", messageSource.getMessage("account.success", null, locale));
        model.addAttribute("redirectMessage", messageSource.getMessage("account.redirect", null, locale));
        return "confirm";
    }
}
