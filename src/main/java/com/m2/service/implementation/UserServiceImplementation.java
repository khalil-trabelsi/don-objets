package com.m2.service.implementation;

import com.m2.dto.UserDto;
import com.m2.model.User;
import com.m2.repository.UserRepository;
import com.m2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto save(UserDto userDto) {
        log.info("Saving new user");
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        return UserDto.fromEntity(userRepository.save(UserDto.toEntity(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        if (optionalUser.isPresent()) {
            return UserDto.fromEntity(userRepository.save(UserDto.toEntity(userDto)));
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public UserDto getUserById(int id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            log.error("Invalid email provided: {}", email);
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        log.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            log.warn("User with ID {} not found.", id);
            throw new IllegalArgumentException("User not found");
        }

    }
}
