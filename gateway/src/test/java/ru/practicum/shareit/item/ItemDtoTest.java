package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.Create;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidationSuccess() {
        final ItemDto itemDto = new ItemDto(1L,"name","saas",true,
                null,null,null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationFailureOnName() {
        final ItemDto itemDto = new ItemDto(1L,null,"saas",true,
                null,null,null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidationFailureOnDescription() {
        final ItemDto itemDto = new ItemDto(1L,"name",null,true,
                null,null,null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidationFailureOnAvailable() {
        final ItemDto itemDto = new ItemDto(1L,"name","saas",null,
                null,null,null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Create.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidationFailureWhenAllParamsIsNull() {
        final ItemDto itemDto = new ItemDto(1L,null,null,null,
                null,null,null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Create.class);
        assertEquals(violations.size(), 3);
    }
}