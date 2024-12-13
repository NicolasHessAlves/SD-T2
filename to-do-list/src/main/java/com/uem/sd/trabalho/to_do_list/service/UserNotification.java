package com.uem.sd.trabalho.to_do_list.service;

import com.uem.sd.trabalho.to_do_list.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserNotification {
    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(NotificationDTO notificationDTO) {
        rabbitTemplate.convertAndSend("notification", notificationDTO);
        log.info("Notification request has been sent for userID: " + notificationDTO.userId().toString());
    }
}
