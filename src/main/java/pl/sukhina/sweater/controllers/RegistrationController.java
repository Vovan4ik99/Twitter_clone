package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sukhina.sweater.models.Role;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.user.UserService;

import java.util.Collections;
import java.util.Map;

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
    public String addUser(User user, Map<String, Object> model) {
        var foundUser = userService.findUserByUsername(user.getUsername());
        if (foundUser != null) {
            model.put("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singletonList(Role.USER));
        userService.createUser(user);
        return "redirect:/login";
    }
}
