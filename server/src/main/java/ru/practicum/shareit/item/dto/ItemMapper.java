package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        ItemDto iDto = new ItemDto();
        iDto.setId(item.getId());
        iDto.setName(item.getName());
        iDto.setDescription(item.getDescription());
        iDto.setAvailable(item.getAvailable());

        if (item.getRequest() != null) {
            iDto.setRequestId(item.getRequest().getId());
        }
        return iDto;
    }

    public static Item toItem(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public static ItemShortDto toShortDto(Item item) {
        return new ItemShortDto(item.getId(), item.getName(), item.getOwner().getId());
    }
}
