package uz.asadbek.course.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.asadbek.course.domain.User;

@Service
public class SessionService {
    private final UserService userService;

    public SessionService(UserService userService) {
        this.userService = userService;
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.findByEmail(userDetails.getUsername());
        }
        return null;
    }
}
