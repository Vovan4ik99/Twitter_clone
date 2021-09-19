package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.user.UserService;

import javax.validation.Valid;

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
    public String addUser( @RequestParam("passwordConfirmation") String passwordConfirmation,
            @Valid User user,
                          BindingResult bindingResult,
                          Model model) {

        if (passwordConfirmation.isEmpty()) {
            model.addAttribute("passwordConfirmationError", "Password confirmation can not be empty!");
            return "registration";
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "registration";
        }

        if (passwordConfirmation.isEmpty() || bindingResult.hasErrors()) {
            var errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            return "registration";
        }

        if (userService.createUser(user) == null) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String active(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("messageType", "alert-success");
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("messageType", "alert-danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
