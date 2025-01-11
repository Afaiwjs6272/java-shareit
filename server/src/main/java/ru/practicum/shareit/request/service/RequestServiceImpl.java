package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Sort sortCreatedDesc = Sort.by(Sort.Direction.DESC, "created");

    @Override
    public ItemRequestDto addRequest(Long userId, ItemRequestDto dto) {
        User user = userExistCheckAndLoad(userId);
        ItemRequest itemRequest = ItemRequestMapper.toModel(dto, user);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getById(Long userId, Long requestId) {
        userExistCheckAndLoad(userId);
        ItemRequest request = itemRequestRepository.findItemRequestById(requestId);
        List<Item> items = itemRepository.findByRequestId(requestId);
        ItemRequestDto res = ItemRequestMapper.toDto(request);
        res.setItems(items.stream().map(ItemMapper::toShortDto).toList());
        return res;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        List<ItemRequest> requests = itemRequestRepository.findItemRequestByRequesterIdNot(userId, sortCreatedDesc);
        return requests.stream().map(ItemRequestMapper::toDto).toList();
    }

    @Override
    public List<ItemRequestDto> getAllByUserId(Long userId) {
        List<ItemRequest> requests = itemRequestRepository.findItemRequestByRequesterId(userId, sortCreatedDesc);

        List<Long> requestIds = requests.stream().map(ItemRequest::getId).toList();

        Map<Long, List<Item>> itemsByRequestId = itemRepository.findByRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return requests.stream().map(ItemRequestMapper::toDto)
                .peek(req -> req.setItems(
                        itemsByRequestId.getOrDefault(req.getId(), List.of()).stream()
                                .map(ItemMapper::toShortDto)
                                .toList()
                ))
                .toList();
    }

    private User userExistCheckAndLoad(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("This user doesn't exist"));
    }
}
