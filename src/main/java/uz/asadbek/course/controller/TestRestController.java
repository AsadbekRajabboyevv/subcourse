package uz.asadbek.course.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.course.model.test.TestCreateDTO;
import uz.asadbek.course.service.TestService;

@RestController
@RequestMapping("/api/test")
public class TestRestController {

    private final TestService testService;

    public TestRestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTest(@RequestBody TestCreateDTO test) {
        testService.createTest(test);
        return ResponseEntity.ok("test qoshildi!");
    }
}
