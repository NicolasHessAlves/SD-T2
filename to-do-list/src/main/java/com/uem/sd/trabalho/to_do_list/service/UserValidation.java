package com.uem.sd.trabalho.to_do_list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidation {
    private final RabbitTemplate rabbitTemplate;

    public Boolean validateUser(final Long userId) {
        log.info("User validation request has been sent for userID: " + userId.toString());
        Boolean response = rabbitTemplate.convertSendAndReceiveAsType("user-validation", userId, new ParameterizedTypeReference<Boolean>() {});
        log.info("User validation response has been received for userID: " + userId.toString() + " valid: " + response.toString());
        return response;
    }
}
