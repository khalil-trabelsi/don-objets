package com.m2.service;

import com.m2.model.Message;
import com.m2.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface MessageService {
    Message sendMessage(User sender, User receiver, String content);
    Page<Message> getMessages(User sender, User receiver, Pageable pageable);
}
