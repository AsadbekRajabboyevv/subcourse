package uz.asadbek.course.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import uz.asadbek.course.model.RegisterRequestDTO;
import uz.asadbek.course.model.UserPositions;

@org.springframework.web.bind.annotation.ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {


    @ModelAttribute
    public void advice(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if(userDetails != null) {
            model.addAttribute("positions", UserPositions.values());
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("register",new RegisterRequestDTO());
            model.addAttribute("role", userDetails.getAuthorities().iterator().next().getAuthority());
        }else {
            model.addAttribute("positions", UserPositions.values());
            model.addAttribute("username", "");
            model.addAttribute("register",new RegisterRequestDTO());
            model.addAttribute("role", "ROLE_USER");
        }
    }
}
