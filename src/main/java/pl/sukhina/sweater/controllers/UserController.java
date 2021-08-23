package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sukhina.sweater.models.Role;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.user.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    final UserService userService;

    @GetMapping
    public String userList(Model model) {
        var users = userService.getUsers();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("{user}")
    public String updateUserForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String updateUser(
            @RequestParam String username,
            @RequestParam("userId") User user,
            @RequestParam Map<String, String> form) {
        user.setUsername(username);
        List<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toList());
        user.getRoles().clear();
        for (String key: form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userService.createUser(user);
        return "redirect:/user";
    }

}
