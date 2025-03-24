package uz.asadbek.course.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class LanguageController {

    @GetMapping("/current-language")
    public String getCurrentLanguage(@RequestHeader(value = "Accept-Language", required = false) String language) {
        Locale locale = (language != null) ? Locale.forLanguageTag(language) : LocaleContextHolder.getLocale();
        return "Hozirgi til: " + locale.getLanguage();
    }
}
