package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserRepository {
    List<User> getAll();

    User addUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    User updateUser(User user);
}
