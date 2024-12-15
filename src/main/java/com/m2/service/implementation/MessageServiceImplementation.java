package com.m2.service.implementation;

import com.m2.model.Message;
import com.m2.model.User;
import com.m2.repository.MessageRepository;
import com.m2.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MessageServiceImplementation implements MessageService {

    @Autowired
    public MessageRepository messageRepository;

    @Override
    public Message sendMessage(User sender, User receiver, String content) {
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }

    @Override
    public Page<Message> getMessages(User sender, User receiver, Pageable pageable) {
        // Utilisation de la méthode avec pagination
        return messageRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId(), pageable);
    }
}
