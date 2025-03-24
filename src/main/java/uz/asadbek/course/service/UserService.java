package uz.asadbek.course.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.course.domain.Course;
import uz.asadbek.course.domain.User;
import uz.asadbek.course.model.RegisterRequestDTO;
import uz.asadbek.course.model.UserDTO;
import uz.asadbek.course.model.UserPositions;
import uz.asadbek.course.repos.CourseRepository;
import uz.asadbek.course.repos.UserRepository;
import uz.asadbek.course.util.NotFoundException;
import uz.asadbek.course.util.UserRoles;


@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final JavaMailSenderImpl mailSender;

    public UserService(final UserRepository userRepository,
                       final CourseRepository courseRepository, JavaMailSenderImpl mailSender) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.mailSender = mailSender;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    @Transactional
    public void update(final Integer id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    @Transactional
    public void delete(final Integer id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        courseRepository.findAllByUsers(user)
                .forEach(course -> course.getUsers().remove(user));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setDescription(user.getDescription());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPosition(user.getPosition());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        UserPositions position = userDTO.getPosition();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setDescription(userDTO.getDescription());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPosition(userDTO.getPosition());
        if (position.equals(UserPositions.BOSHQA)
                ||position.equals(UserPositions.OTA_ONA)
                ||position.equals(UserPositions.TALABA)
                ||position.equals(UserPositions.OQUVCHI)) {
            user.setRole(UserRoles.ROLE_USER.toString());
        } else if (position.equals(UserPositions.OQITUVCHI)) {
            user.setRole(UserRoles.ROLE_TEACHER.toString());
        }else{
            user.setRole(UserRoles.ROLE_ADMIN.toString());
        }
        return user;
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public Long countAllUsers(){
        return userRepository.count();
    }

    public User findByEmail(String username) {
      return userRepository.findByEmail(username).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void enrollUserToCourse(Integer userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        user.getCourses().add(course);
        course.getUsers().add(user);
        userRepository.save(user);
    }

    @Transactional
    public void register(RegisterRequestDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Bu email allaqachon mavjud: " + registerDTO.getEmail());
        }
        String token = generateConfirmationToken();
        UserPositions position = registerDTO.getPosition();
        User user = new User();
        user.setConfirmationToken(token);
        user.setEnabled(false);
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setBirthDate(registerDTO.getBirthDate());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setPassword(registerDTO.getPassword());
        if (position.equals(UserPositions.BOSHQA)
                ||position.equals(UserPositions.OTA_ONA)
                ||position.equals(UserPositions.TALABA)
                ||position.equals(UserPositions.OQUVCHI)) {
            user.setRole(UserRoles.ROLE_USER.toString());
        } else if (position.equals(UserPositions.OQITUVCHI)) {
            user.setRole(UserRoles.ROLE_TEACHER.toString());
        }else{
            user.setRole(UserRoles.ROLE_ADMIN.toString());
        }
        user.setPosition(registerDTO.getPosition());
        userRepository.save(user);
        String confirmationUrl = "http://localhost:8081/auth/confirm?confirmToken=" + token;
        String message = "Akkauntingizni tasdiqlash uchun quyidagi linkga o'ting: \n" + confirmationUrl;
        sendEmail(registerDTO.getEmail(), "Email tasdiqlash", message);
    }
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }


    public String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }
    @Transactional
    public boolean confirmUser(String token) {
        User user = userRepository.findByConfirmationToken((token));
        if (user != null) {
            user.setEnabled(true);
            user.setConfirmationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }


}
