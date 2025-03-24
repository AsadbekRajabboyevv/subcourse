package uz.asadbek.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.asadbek.course.domain.User;
import uz.asadbek.course.model.CustomUserDetails;
import uz.asadbek.course.repos.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Foydalanuvchi topilmadi: " + email));

        return new CustomUserDetails(user.getEmail(), user.getPassword(), user.isEnabled(), user.getRole());
    }
}
