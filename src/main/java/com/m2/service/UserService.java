package com.m2.service;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.UserDto;
import com.m2.model.User;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto getUserById(int id);

    UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers();

    void delete(int id);

    void AddRoleToUser(String roleName, String email);

    User loadUserByUsername(String username);

    List<AdvertisementDto> getAdvertisementsByUserId(int id);

}
