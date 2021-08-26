package pl.sukhina.sweater.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.sukhina.sweater.models.Message;
import pl.sukhina.sweater.models.User;
import pl.sukhina.sweater.services.message.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class MainController {

    final MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam String tag, Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Message message = new Message(text, tag, user);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            message.setFilename(resultFilename);
        }
        messageService.createMessage(message);
        var messages = messageService.getMessages();
        model.addAttribute("messages", messages);
        return "main";
    }

}
