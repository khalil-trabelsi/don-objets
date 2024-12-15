package com.m2.api;

import com.m2.dto.MessageDto;
import com.m2.model.Message;
import com.m2.model.User;
import com.m2.repository.UserRepository;
import com.m2.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(18);
        sender.setUsername("Sender");

        receiver = new User();
        receiver.setId(17);
        receiver.setUsername("Receiver");
    }

    @Test
    void testGetMessages() throws Exception {
        when(userRepository.findById(18)).thenReturn(Optional.of(sender));
        when(userRepository.findById(17)).thenReturn(Optional.of(receiver));

        Message message1 = new Message(1, sender, receiver, "Hello, this is a message!", LocalDateTime.now());
        Message message2 = new Message(2, sender, receiver, "Another message!", LocalDateTime.now());

        Page<Message> messagePage = new PageImpl<>(Arrays.asList(message1, message2), PageRequest.of(0, 8), 2);
        when(messageService.getMessages(sender, receiver, PageRequest.of(0, 8))).thenReturn(messagePage);

        mockMvc.perform(get("/api/messages")
                        .param("senderId", "18")
                        .param("receiverId", "17")
                        .param("page", "0")
                        .param("size", "8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello, this is a message!"))
                .andExpect(jsonPath("$[1].content").value("Another message!"))
                .andExpect(jsonPath("$[0].senderId").value(18))
                .andExpect(jsonPath("$[0].receiverId").value(17))
                .andExpect(jsonPath("$[1].senderId").value(18))
                .andExpect(jsonPath("$[1].receiverId").value(17));

        verify(userRepository, times(1)).findById(18);
        verify(userRepository, times(1)).findById(17);
        verify(messageService, times(1)).getMessages(sender, receiver, PageRequest.of(0, 8));
    }

    @Test
    void testSendMessage() throws Exception {
        MessageDto messageDto = new MessageDto();
        messageDto.setSenderId(18);
        messageDto.setReceiverId(17);
        messageDto.setContent("Hello, this is a message!");

        when(messageService.sendMessage(any(User.class), any(User.class), eq("Hello, this is a message!"))).thenReturn(new Message(1, sender, receiver, "Hello, this is a message!", LocalDateTime.now()));

        mockMvc.perform(post("/api/messages").contentType(MediaType.APPLICATION_JSON).content("{\"senderId\":18, \"receiverId\":17, \"content\":\"Hello, this is a message!\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.content").value("Hello, this is a message!")).andExpect(jsonPath("$.senderId").value(18)).andExpect(jsonPath("$.receiverId").value(17)).andExpect(jsonPath("$.timestamp").exists());

        verify(messageService, times(1)).sendMessage(any(User.class), any(User.class), eq("Hello, this is a message!"));
    }
}
