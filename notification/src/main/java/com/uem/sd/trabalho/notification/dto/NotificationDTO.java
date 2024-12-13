package com.uem.sd.trabalho.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record NotificationDTO(
        @NotNull
        @PositiveOrZero
        Long userId,

        @NotNull
        @NotBlank
        @Size(min = 1, max = 512)
        String description,

        @NotNull
        Boolean done
) {
}
