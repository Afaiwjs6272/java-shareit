package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookItemRequestDto> jacksonTester;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusDay = now.plusDays(1);
        final BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(
                1L,
                now,
                nowPlusDay
        );
        JsonContent<BookItemRequestDto> json = jacksonTester.write(bookItemRequestDto);
        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2024-12-22T21:04:20\",\"end\":\"2024-12-22T21:04:20\"}";
        BookItemRequestDto dto = jacksonTester.parseObject(json);
        assertThat(dto.getItemId()).isEqualTo(1);
    }

    @Test
    public void testValidationSuccess() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(10));
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookItemRequestDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationFailureOnStart() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1));
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookItemRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidationFailureOnEnd() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, LocalDateTime.now(), LocalDateTime.now());
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookItemRequestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidationFailureWhenAll() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookItemRequestDto);
        assertEquals(violations.size(), 2);
    }
}
