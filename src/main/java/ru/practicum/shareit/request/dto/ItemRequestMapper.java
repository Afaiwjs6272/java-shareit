package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getDescription(),
                itemRequest.getRequestor() != null ? itemRequest.getRequestor().getId() : null,
                itemRequest.getCreated()
        );
    }
}
