package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.user.UserService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    final UserService userService;

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(User user, Model model) {

        if (userService.createUser(user) == null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }
}
