package ru.practicum.shareit.user.dto;

import lombok.Value;
import ru.practicum.shareit.user.model.User;

@Value
public class UserMapper {
    public static UserDto toUserDto(final User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(final UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
}

