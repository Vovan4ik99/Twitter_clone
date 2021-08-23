package pl.sukhina.sweater.services.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.sukhina.sweater.models.Message;
import pl.sukhina.sweater.repositories.MessageRepository;

import java.util.List;

@Primary
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    final MessageRepository messageRepository;

    @Override
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessage(Long id) {
        return messageRepository.getById(id);
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Long id, Message messageToSave) {
        messageToSave.setId(id);
        return messageRepository.save(messageToSave);
    }

    @Override
    public Message deleteMessage(Long id) {
        var foundMessage = getMessage(id);
        messageRepository.deleteById(id);
        return foundMessage;
    }

    @Override
    public List<Message> findAllByTag(String tag) {
        return messageRepository.findAllByTag(tag);
    }
}
