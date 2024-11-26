package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    void removeUser(Long id);

    UserDto updateUser(UserDto userDto, Long id);

    UserDto getUserById(Long id);

    List<UserDto> getAll();
}
