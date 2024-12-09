package com.m2.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.m2.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Date registeredAt;
    @JsonIgnore
    private List<AdvertisementDto> advertisements;

    public static UserDto fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .registeredAt(user.getRegisteredAt())
                .build();
    }

    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRegisteredAt(userDto.getRegisteredAt());

        return user;
    }

}
