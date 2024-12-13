package com.uem.sd.trabalho.to_do_list.dto;

public record NotificationDTO(
        Long userId,
        String description,
        Boolean done
) {
}
