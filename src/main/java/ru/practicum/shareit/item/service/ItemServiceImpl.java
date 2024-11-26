package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    @Override
    public List<ItemDto> getAllForOwner(Long userId) {
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User user = checkValidUser(userId);
        Item item = itemRepository.findById(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException("Current user doesn't have this item");
        }

        Item newItem = ItemMapper.toItem(itemDto);
        newItem.setOwner(user);

        if (newItem.getId() == null) {
            newItem.setId(item.getId());
        }
        if (newItem.getName() == null) {
            newItem.setName(item.getName());
        }
        if (newItem.getDescription() == null) {
            newItem.setDescription(item.getDescription());
        }
        if (newItem.getAvailable() == null) {
            newItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.update(newItem));
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        checkValidUser(userId);
        return ItemMapper.toItemDto(itemRepository.findById(itemId));
    }

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = checkValidUser(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.add(item));
    }

    @Override
    public List<ItemDto> findByKeyWord(Long userId, String text) {
        checkValidUser(userId);
        return itemRepository.findByText(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User checkValidUser(Long userId) {
        return userRepository.getUserById(userId);
    }
}
