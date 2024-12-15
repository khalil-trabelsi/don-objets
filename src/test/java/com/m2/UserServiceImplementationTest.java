package com.m2;

import com.m2.dto.AdvertisementDto;
import com.m2.dto.UserDto;
import com.m2.model.User;
import com.m2.repository.AdvertisementRepository;
import com.m2.repository.UserRepository;
import com.m2.service.implementation.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdvertisementRepository advertisementRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("rawPassword");
        userDto.setRegisteredAt(null);

        User user = new User();
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUser = userService.save(userDto);

        assertNotNull(savedUser);
        assertEquals("encodedPassword", userDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserFound() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedUser = userService.update(userDto);

        assertNotNull(updatedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.update(userDto));
    }

    @Test
    void testGetUserById_UserFound() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(1);

        assertNotNull(userDto);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1));
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.delete(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUser_UserDoesNotExist() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.delete(1));
    }

    @Test
    void testGetAdvertisementsByUserId() {
        when(advertisementRepository.findAllByUserId(1)).thenReturn(List.of());

        List<AdvertisementDto> advertisements = userService.getAdvertisementsByUserId(1);

        assertNotNull(advertisements);
        verify(advertisementRepository, times(1)).findAllByUserId(1);
    }
}
