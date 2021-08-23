package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sukhina.sweater.models.Message;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.message.MessageService;

@RequiredArgsConstructor
@Controller
public class MainController {

    final MessageService messageService;

    @GetMapping
    public String greeting  (Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        var messages = messageService.getMessages();
        if (filter != null && !filter.isEmpty()) {
            messages = messageService.findAllByTag(filter);
        } else {
            messages = messageService.getMessages();
        }
        model.addAttribute("filter", filter);
        model.addAttribute("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String sendMessage(
            @AuthenticationPrincipal User user, @RequestParam String text,
            @RequestParam String tag, Model model) {
        Message message = new Message(text, tag, user);
        messageService.createMessage(message);
        var messages = messageService.getMessages();
        model.addAttribute("messages", messages);
        return "main";
    }

}
