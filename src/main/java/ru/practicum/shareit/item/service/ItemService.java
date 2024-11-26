package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllForOwner(Long userId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItem(Long userId, Long itemId);

    ItemDto addItem(Long userId, ItemDto itemDto);

    List<ItemDto> findByKeyWord(Long userId, String text);
}
