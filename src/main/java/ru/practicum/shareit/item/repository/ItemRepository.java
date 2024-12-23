package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId);

    @Query("select i " +
            "from Item as i " +
            "where i.available = true " +
            "and (i.name ilike concat('%', ?1, '%') or i.description ilike concat('%', ?1, '%'))")
    List<Item> findByText(String text);
}
