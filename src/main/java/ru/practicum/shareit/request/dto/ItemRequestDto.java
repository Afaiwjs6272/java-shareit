package ru.practicum.shareit.request.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ItemRequestDto {

    String description;
    Long requestor;
    LocalDateTime created;

    public ItemRequestDto(String description, Long requestor, LocalDateTime created) {
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }
}