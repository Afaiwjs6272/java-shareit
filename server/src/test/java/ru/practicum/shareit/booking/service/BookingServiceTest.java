package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingValidationException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final EntityManager entityManager;

    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        userDto = userService.add(new UserDto(null, "aa", "dad@example.ru"));
        itemDto = itemService.createItemByUser(userDto.getId(), new ItemDto(null,
                "item", "description", true,
                null, null, null, null));
    }

    @Test
    public void failureCreateBookingWhenStartEqualsEnd() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now(), itemDto.getId(),
                Status.APPROVED, itemDto, userDto);
        assertThrows(BookingValidationException.class, () -> bookingService.createBooking(userDto.getId(), bookingDto));
    }

    @Test
    public void failureCreateBookingWhenStartIsAfterEnd() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now(), itemDto.getId(),
                Status.APPROVED, itemDto, userDto);
        assertThrows(BookingValidationException.class, () -> bookingService.createBooking(userDto.getId(), bookingDto));
    }

    @Test
    public void successWhenCreateBooking() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), itemDto.getId(),
                Status.WAITING, itemDto, userDto);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        TypedQuery<Booking> query = entityManager.createQuery("SELECT b from Booking as b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();

        assertThat(booking, notNullValue());
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
    }

    @Test
    public void userNotExistsWhenCreateBooking() {
        UserDto userDto1 = new UserDto(2L, "fhshs", "fsd@mail.ru");
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), itemDto.getId(),
                Status.WAITING, itemDto, userDto1);

        assertThrows(UserNotFoundException.class,
                () -> bookingService.createBooking(userDto1.getId(), bookingDto));
    }

    @Test
    public void successWhenGetBooking() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), itemDto.getId(),
                Status.WAITING, itemDto, userDto);

        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        BookingDto result = bookingService.getBooking(userDto.getId(), bookingDto.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(bookingDto.getId()));
        assertThat(result.getBooker(), equalTo(userDto));
    }


    @Test
    public void failWhenGetBookingByWrongUser() {
        UserDto userDto1 = new UserDto(2L, "fhshs", "fsd@mail.ru");
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), itemDto.getId(),
                Status.WAITING, itemDto, userDto);

        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBooking(userDto1.getId(), bookingDto.getId()));
    }

    @Test
    public void wrongIdBookingConfirmFail() {
        assertThrows(BookingNotFoundException.class,
                () -> bookingService.confirmBooking(userDto.getId(), -1L, true));
    }

    @Test
    public void successWhenConfirmBooking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        BookingDto result = bookingService.confirmBooking(userDto.getId(), bookingDto.getId(), true);

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(bookingDto.getId()));
        assertThat(result.getStatus(), equalTo(Status.APPROVED));

        bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        result = bookingService.confirmBooking(userDto.getId(), bookingDto.getId(), false);
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(bookingDto.getId()));
        assertThat(result.getStatus(), equalTo(Status.REJECTED));
    }

    @Test
    void getUserBookings() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        List<BookingDto> result = bookingService.getUserBookings(userDto.getId(), State.ALL);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getId(), equalTo(bookingDto.getId()));

        result = bookingService.getUserBookings(userDto.getId(), State.CURRENT);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));

        result = bookingService.getUserBookings(userDto.getId(), State.FUTURE);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(0));

        result = bookingService.getUserBookings(userDto.getId(), State.PAST);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(0));

        result = bookingService.getUserBookings(userDto.getId(), State.WAITING);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));

        result = bookingService.getUserBookings(userDto.getId(), State.REJECTED);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(0));
    }

    @Test
    void getUserItemBookings() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        BookingDto bookingDto = new BookingDto(null, now, nowPlusDay,
                itemDto.getId(), null, null, null);
        bookingDto = bookingService.createBooking(userDto.getId(), bookingDto);

        List<BookingDto> result = bookingService.getUserItemBookings(userDto.getId(), State.ALL);

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getId(), equalTo(bookingDto.getId()));
    }
}
