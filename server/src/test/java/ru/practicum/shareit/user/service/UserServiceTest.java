package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {
    private final EntityManager entityManager;
    private final UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = userService.add(new UserDto(null, "john", "j@example.ru"));
    }

    @Test
    public void successGetAll() {
        List<UserDto> users = userService.getAll();

        assertNotNull(users);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getName(), userDto.getName());
    }

    @Test
    public void userNotExistsWhenGetById() {
        assertThrows(UserNotFoundException.class, () -> userService.getById(-1L));
    }

    @Test
    public void successGetById() {
        UserDto userDto1 = userService.getById(userDto.getId());

        assertNotNull(userDto1);
        assertEquals(userDto1.getName(), userDto.getName());
    }

    @Test
    public void successAddUser() {
        UserDto userDto1 = new UserDto(null, "john", "j1@example.ru");

        userDto1 = userService.add(userDto1);

        TypedQuery<User> query = entityManager.createQuery("SELECT u from User as u where u.email = :email", User.class);
        User result = query.setParameter("email", userDto1.getEmail()).getSingleResult();

        assertNotNull(result);
        assertEquals(result.getName(), userDto1.getName());
        assertEquals(result.getEmail(), userDto1.getEmail());
    }

    @Test
    void update() {
        UserDto updatedUser = new UserDto(userDto.getId(), "john", null);

        updatedUser = userService.update(userDto.getId(), updatedUser);

        TypedQuery<User> query = entityManager.createQuery("SELECT u from User as u where u.id = :id", User.class);
        User result = query.setParameter("id", updatedUser.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(result.getName(), updatedUser.getName());
        assertEquals(result.getEmail(), userDto.getEmail());

        updatedUser.setName(null);
        updatedUser.setEmail("john2@example.ru");

        updatedUser = userService.update(userDto.getId(), updatedUser);

        result = query.setParameter("id", updatedUser.getId()).getSingleResult();

        assertNotNull(result);
        assertEquals(result.getName(), userDto.getName());
        assertEquals(result.getEmail(), updatedUser.getEmail());
    }

    @Test
    void delete() {
        userService.delete(userDto.getId());

        TypedQuery<User> query = entityManager.createQuery("SELECT u from User as u where u.email = :email", User.class);

        assertThrows(NoResultException.class, () -> query.setParameter("email", userDto.getEmail()).getSingleResult());
    }
}