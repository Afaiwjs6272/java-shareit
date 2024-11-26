package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDto {
    private Long id;
    @NotEmpty(groups = Create.class)
    private String name;
    @NotEmpty(groups = Create.class)
    @NotBlank(groups = Create.class)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;
}