package com.uem.sd.trabalho.to_do_list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ToDoRequestDTO(
        @NotNull
        @NotBlank
        @Size(min = 1, max = 512)
        String description,
        Boolean done
) {
}
