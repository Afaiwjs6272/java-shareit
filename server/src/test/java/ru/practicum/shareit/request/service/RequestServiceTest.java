package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceTest {
    private final EntityManager entityManager;
    private final RequestService itemRequestService;
    private final UserService userService;

    private UserDto userDto;

    private ItemRequestDto itemRequestDto;


    @BeforeEach
    void setUp() {
        userDto = userService.add(new UserDto(null, "john", "j@example.ru"));
        itemRequestDto = itemRequestService.addRequest(userDto.getId(), new ItemRequestDto(null,
                "item request", null, LocalDateTime.now(), null));
    }

    @Test
    public void successAddRequest() {
        ItemRequestDto dto = new ItemRequestDto(null, "item request uniq",
                null, LocalDateTime.now(), null);

        dto = itemRequestService.addRequest(userDto.getId(), dto);

        TypedQuery<ItemRequest> query = entityManager
                .createQuery("SELECT i from ItemRequest as i where i.description = :desc", ItemRequest.class);
        ItemRequest result = query.setParameter("desc", dto.getDescription()).getSingleResult();

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getDescription(), result.getDescription());
    }

    @Test
    void getAll() {
        List<ItemRequestDto> dtos = itemRequestService.getAllRequests(userDto.getId() + 1);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(itemRequestDto.getId(), dtos.getFirst().getId());
    }

    @Test
    public void successGetById() {
        ItemRequestDto itemRequestDto1 = itemRequestService.getById(userDto.getId(), itemRequestDto.getId());

        assertNotNull(itemRequestDto1);
        assertEquals(itemRequestDto1.getRequester(), itemRequestDto.getRequester());
    }

    @Test
    public void successGetAllByUserId() {
        List<ItemRequestDto> itemRequestDtoList = itemRequestService.getAllByUserId(userDto.getId());

        assertNotNull(itemRequestDtoList);
        assertEquals(itemRequestDtoList.size(), 1);
        assertEquals(itemRequestDtoList.get(0).getRequester(), itemRequestDto.getRequester());
    }
}
