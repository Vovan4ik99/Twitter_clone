package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sukhina.sweater.models.Message;
import pl.sukhina.sweater.repositories.MessageRepository;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MainController {

    final MessageRepository messageRepository;

    @GetMapping
    public String greeting  (Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        var messages = messageRepository.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/main")
    public String sendMessage(@RequestParam String text,
                              @RequestParam String tag,
                              Map<String, Object> model) {
        Message message = new Message(text, tag);
        messageRepository.save(message);
        var messages = messageRepository.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("filter")
    public String searchMessagesByFilter(@RequestParam String filter,
                                         Map<String, Object> model) {
        List<Message> messages;
        if (filter != null && filter.isEmpty()) {
            messages = messageRepository.findAllByTag(filter);
        } else {
            messages = messageRepository.findAll();
        }
        model.put("messages", messages);
        return "main";
    }
}
