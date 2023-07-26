package ru.practicum.main.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.dto.UserShortDto;
import ru.practicum.main.user.entity.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public UserDto toDtoUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
