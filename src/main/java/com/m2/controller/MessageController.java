package com.m2.controller;

import com.m2.model.Message;
import com.m2.model.User;
import com.m2.repository.MessageRepository;
import com.m2.repository.UserRepository;
import com.m2.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page; // Correct import
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @GetMapping
    public String getMessages(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "8") int size,
            @RequestParam(name = "senderId", defaultValue = "0") int senderId,
            @RequestParam(name = "receiverId", defaultValue = "0") int receiverId,
            Authentication authentication
    ) {
        if (authentication == null) {
            return "redirect:/login";
        }
        Page<Message> messagePage = messageRepository.findBySenderIdAndReceiverId(
                senderId, receiverId, PageRequest.of(page, size));

        model.addAttribute("messages", messagePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("senderId", senderId);
        model.addAttribute("receiverId", receiverId);
        return "messageList";
    }


    @PostMapping("/send")
    public String sendMessage(
            @RequestParam(name = "senderId", defaultValue = "0") int senderId,
            @RequestParam(name = "receiverId", defaultValue = "0") int receiverId,
            @RequestParam(name = "message", defaultValue = "0") String message,
            Authentication authentication

    ) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        messageService.sendMessage(sender, receiver, message);

        return "redirect:/messages?senderId=" + senderId + "&receiverId=" + receiverId + "&page=0&size=8";
    }
}
