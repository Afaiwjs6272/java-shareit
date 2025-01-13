package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest ir) {
        return new ItemRequestDto(ir.getId(),
                ir.getDescription(), UserMapper.toUserDto(ir.getRequester()), ir.getCreated(), null);
    }

    public static ItemRequest toModel(ItemRequestDto dto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }
}
