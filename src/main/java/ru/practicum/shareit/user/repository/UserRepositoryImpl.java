package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public User addUser(User user) {
        isEmailExist(user.getEmail(), null);
        checkValidUser(user);
        user.setId(id);
        users.put(user.getId(), user);
        increaseId();
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("User not found");
        }
        isEmailExist(user.getEmail(), user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("user with id = " + id + " not found");
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return users.values()
                .stream()
                .toList();
    }

    private void isEmailExist(String email, Long userId) {
        User user = users.values().stream()
                .filter(u -> u != null && u.getId() != null && !u.getId().equals(userId))
                .filter(u -> u.getEmail() != null && u.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (user != null) {
            throw new EmailException("Email already exists");
        }
    }

    private void checkValidUser(User user) {
        if (user.getEmail().isEmpty()) {
            throw new UserNotFoundException("email should exist");
        }
    }

    private void increaseId() {
        id++;
    }
}
