package com.uem.sd.trabalho.to_do_list.dto;

import jakarta.validation.constraints.NotNull;

public record ToDoResponseDTO(
        Long id,
        String description,
        boolean done
) {
}
