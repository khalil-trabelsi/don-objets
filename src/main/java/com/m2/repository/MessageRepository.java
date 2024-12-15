package com.m2.repository;


import com.m2.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface MessageRepository extends JpaRepository<Message, Integer> {
    Page<Message> findBySenderIdAndReceiverId(int senderId, int receiverId, Pageable pageable);
}

