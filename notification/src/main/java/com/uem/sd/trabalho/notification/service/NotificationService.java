package com.uem.sd.trabalho.notification.service;

import com.uem.sd.trabalho.notification.dto.NotificationDTO;
import com.uem.sd.trabalho.notification.dto.UserDTO;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@EnableRabbit
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final Validator validator;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "notification")
    void sendNotification(NotificationDTO notificationDTO) {
        if(!validator.validate(notificationDTO).isEmpty()) {
            log.error("Notification validation error.");
            return;
        }

        UserDTO userDTO = rabbitTemplate.convertSendAndReceiveAsType("get-user", notificationDTO.userId(), new ParameterizedTypeReference<UserDTO>() {});

        log.info("User get request has been sent for userID: " + notificationDTO.userId().toString());

        if(!validator.validate(userDTO).isEmpty()) {
            log.error("User validation error.");
            return;
        }

        log.info("Sending notification to " + userDTO.name() + " with email: " + userDTO.email() +
                "\nTo-do:\nDescription: " + notificationDTO.description() + "\nDone: " + notificationDTO.done() +
                "\n");

    }
}
