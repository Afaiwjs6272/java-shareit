package ru.practicum.shareit.booking.predicate;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.QBooking;

import java.time.LocalDateTime;

public final class BookingPredicate {
    private BookingPredicate() {
    }

    private static BooleanExpression byBookerId(Long userId) {
        return QBooking.booking.booker.id.eq(userId);
    }

    private static BooleanExpression byItemOwnerId(Long userId) {
        return QBooking.booking.item.owner.id.eq(userId);
    }

    private static BooleanExpression byState(State state) {
        if (state.equals(State.WAITING)) {
            return QBooking.booking.status.eq(Status.WAITING);
        }
        if (state.equals(State.REJECTED)) {
            return QBooking.booking.status.eq(Status.REJECTED);
        }
        if (state.equals(State.CURRENT)) {
            return QBooking.booking.start.before(LocalDateTime.now()).and(QBooking.booking.end.after(LocalDateTime.now()));
        }
        if (state.equals(State.FUTURE)) {
            return QBooking.booking.start.after(LocalDateTime.now());
        }
        if (state.equals(State.PAST)) {
            return QBooking.booking.end.before(LocalDateTime.now());
        }
        return null;
    }

    public static Predicate bookerAndState(Long userId, State state) {
        return byBookerId(userId).and(byState(state));
    }

    public static Predicate itemOwnerAndState(Long userId, State state) {
        return byItemOwnerId(userId).and(byState(state));
    }

    public static OrderSpecifier<LocalDateTime> orderByStart() {
        return QBooking.booking.start.desc();
    }
}