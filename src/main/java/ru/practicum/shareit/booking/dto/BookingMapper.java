package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

public class BookingMapper {
        public static BookingDto toDto(Booking booking) {
            return BookingDto.builder()
                    .id(booking.getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .itemId(booking.getItem().getId())
                    .item(ItemMapper.toItemDto(booking.getItem()))
                    .booker(UserMapper.toUserDto(booking.getBooker()))
                    .status(booking.getStatus())
                    .build();
        }
}