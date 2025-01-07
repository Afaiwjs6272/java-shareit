package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest ir) {
        return new ItemRequestDto(ir.getId(),
                ir.getDescription(), UserMapper.toUserDto(ir.getRequester()), ir.getCreated(), null);
    }
}
