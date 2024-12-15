package com.m2.dto;
import com.m2.model.Message;
import com.m2.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private int id;
    private int senderId;
    private int receiverId;
    private String content;
    private LocalDateTime timestamp;

    public static MessageDto fromEntity(Message message) {
        if (message == null) return null;

        return MessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .receiverId(message.getReceiver().getId())
                .senderId(message.getSender().getId())
                .build();
    }

    public static Message toEntity(MessageDto messageDto, User sender, User receiver) {
        if (messageDto == null) return null;

        return Message.builder()
                .id(messageDto.getId())
                .content(messageDto.getContent())
                .timestamp(messageDto.getTimestamp())
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public List<MessageDto> mapToDtoList(Collection<Message> messages) {
        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
