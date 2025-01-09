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
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getById(Long userId, Long requestId) {
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
        List<Item> items = itemRepository.findByRequestIdIn(requests.stream().map(ItemRequest::getId).toList());
        return requests.stream().map(ItemRequestMapper::toDto)
                .peek(req -> req.setItems(items.stream()
                        .filter(i -> i.getRequest().getId().equals(req.getId()))
                        .map(ItemMapper::toShortDto)
                        .toList()))
                .toList();
    }

    private User userExistCheckAndLoad(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("This user doesn't exist"));
    }
}
