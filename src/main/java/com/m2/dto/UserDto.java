package com.m2.dto;

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
    private String password;
    private Date registeredAt;
    private List<AdvertisementDto> advertisements;

    public static UserDto fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .registeredAt(user.getRegisteredAt())
                .advertisements(
                        user.getAdvertisements() != null ?
                                user.getAdvertisements().stream()
                                        .map(AdvertisementDto::fromEntity)
                                        .collect(Collectors.toList()) :
                                null
                )
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

        if (userDto.getAdvertisements() != null) {
            user.setAdvertisements(
                    userDto.getAdvertisements().stream()
                            .map(AdvertisementDto::toEntity)
                            .collect(Collectors.toList())
            );
        }

        return user;
    }
}
