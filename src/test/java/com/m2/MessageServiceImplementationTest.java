package com.m2;

import com.m2.model.Message;
import com.m2.model.User;
import com.m2.repository.MessageRepository;
import com.m2.service.implementation.MessageServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplementationTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImplementation messageService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1);

        receiver = new User();
        receiver.setId(2);
    }

    @Test
    void sendMessage_SaveMessage() {
        String content = "Hello, World!";
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = messageService.sendMessage(sender, receiver, content);

        assertNotNull(result);
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());
        assertEquals(content, result.getContent());

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository, times(1)).save(messageCaptor.capture());

        Message savedMessage = messageCaptor.getValue();
        assertEquals(sender, savedMessage.getSender());
        assertEquals(receiver, savedMessage.getReceiver());
        assertEquals(content, savedMessage.getContent());
        assertNotNull(savedMessage.getTimestamp());
    }


    @Test
    void getMessages() {
        Pageable pageable = mock(Pageable.class);
        Message message1 = new Message();
        message1.setSender(sender);
        message1.setReceiver(receiver);
        message1.setContent("Message 1");

        Message message2 = new Message();
        message2.setSender(sender);
        message2.setReceiver(receiver);
        message2.setContent("Message 2");

        List<Message> messages = List.of(message1, message2);
        Page<Message> page = new PageImpl<>(messages);

        when(messageRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId(), pageable))
                .thenReturn(page);

        Page<Message> result = messageService.getMessages(sender, receiver, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(message1.getContent(), result.getContent().get(0).getContent());
        assertEquals(message2.getContent(), result.getContent().get(1).getContent());
        verify(messageRepository, times(1)).findBySenderIdAndReceiverId(sender.getId(), receiver.getId(), pageable);
    }
}
