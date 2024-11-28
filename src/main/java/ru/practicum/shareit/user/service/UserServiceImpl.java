package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("new user was added");
        return UserMapper.toUserDto(userRepository.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public void removeUser(Long id) {
        userRepository.deleteUser(id);
        log.info("User with id = {} deleted", id);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = userRepository.getUserById(id);
        User updatedUser = UserMapper.toUser(userDto);
        if (updatedUser.getId() == null) {
            updatedUser.setId(user.getId());
        }
        if (updatedUser.getName() == null) {
            updatedUser.setName(user.getName());
        }
        if (updatedUser.getEmail() == null) {
            updatedUser.setEmail(user.getEmail());
        }
        log.info("user with id = {} was updated", id);
        return UserMapper.toUserDto(userRepository.updateUser(updatedUser));
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(userRepository.getUserById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }
}
