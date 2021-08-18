package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sukhina.sweater.models.Message;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.message.MessageService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MainController {

    final MessageService messageService;

    @GetMapping
    public String greeting  (Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        var messages = messageService.getMessages();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String sendMessage(
            @AuthenticationPrincipal User user, @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model) {
        Message message = new Message(text, tag, user);
        messageService.createMessage(message);
        var messages = messageService.getMessages();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("filter")
    public String searchMessagesByFilter(@RequestParam String filter,
                                         Map<String, Object> model) {
        List<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageService.findAllByTag(filter);
        } else {
            messages = messageService.getMessages();
        }
        model.put("messages", messages);
        return "main";
    }
}
