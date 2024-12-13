package com.uem.sd.trabalho.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotNull
        @NotBlank
        @Size(min = 1, max = 255)
        String name,
        @NotNull
        @NotBlank
        @Size(min = 1, max = 255)
        @Email
        String email
) {
}
