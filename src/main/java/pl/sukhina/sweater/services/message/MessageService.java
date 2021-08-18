package pl.sukhina.sweater.services.message;

import pl.sukhina.sweater.models.Message;

import java.util.List;

public interface MessageService {

    List<Message> getMessages();

    Message getMessage(Long id);

    Message createMessage(Message message);

    Message updateMessage(Long id, Message messageToSave);

    Message deleteMessage(Long id);

    List<Message> findAllByTag(String tag);
}
