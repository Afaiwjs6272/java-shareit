package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User addUser(User user) {
        checkValidUser(user);
        isEmailExist(user.getEmail(), null);
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
        checkValidUser(user);
        isEmailExist(user.getEmail(), user.getId());
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("user not found");
        }
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return users.values()
                .stream()
                .toList();
    }

    private void checkValidUser(User user) {
        if (user.getEmail() == null) {
            throw new RuntimeException("user not valid");
        }
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


    private void increaseId() {
        id++;
    }
}
