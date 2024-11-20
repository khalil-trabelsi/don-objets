package com.m2.service;

import com.m2.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);
    UserDto update(UserDto userDto);
    UserDto getUserById(int id);
    UserDto getUserByEmail(String email);
    List<UserDto> getAllUsers();
    void delete(int id);
}
