package com.m2.controller.api;

import com.m2.dto.MessageDto;
import com.m2.model.Message;
import com.m2.model.User;
import com.m2.repository.UserRepository;
import com.m2.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
@Slf4j
public class MessageApi {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<MessageDto>> getMessages(@RequestParam int senderId,
                                                        @RequestParam int receiverId,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "8") int size) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Message> messagePage = messageService.getMessages(sender, receiver, pageable);

        // Convertir les messages en DTOs
        List<MessageDto> messageDtos = messagePage.getContent().stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageDtos);
    }


    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto request) {
        User sender = userRepository.findById(request.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId()).orElseThrow(() -> new RuntimeException("Receiver not found"));
        Message message = messageService.sendMessage(sender, receiver, request.getContent());
        MessageDto messageDto = MessageDto.fromEntity(message);
        return ResponseEntity.ok(messageDto);
    }

}
