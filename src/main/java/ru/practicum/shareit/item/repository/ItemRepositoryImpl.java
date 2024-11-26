package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items =  new HashMap<>();
    private Long id = 0L;

    @Override
    public List<Item> findAllByOwnerId(Long userId) {
        return items.values().stream()
                .filter(item -> userId.equals(item.getOwner().getId()))
                .toList();
    }

    @Override
    public List<Item> findByText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
            return items.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase())
                    || item.getName().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }

    @Override
    public Item findById(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new ItemNotFoundException("item with id = " + id + " not found");
        }
        return item;
    }

    @Override
    public Item add(Item item) {
        item.setId(id);
        items.put(item.getId(), item);
        increaseId();
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    private void increaseId() {
        id++;
    }
}
