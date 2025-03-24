package uz.asadbek.course.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/course/**")
                .addResourceLocations("file:" + "/uploads/course/");
        registry.addResourceHandler("/uploads/lesson/**")
                .addResourceLocations("file:" + "/uploads/lesson/");

    }
}
