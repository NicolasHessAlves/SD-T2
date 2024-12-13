package com.uem.sd.trabalho.user.service;

import com.uem.sd.trabalho.user.dto.UserRequestDTO;
import com.uem.sd.trabalho.user.dto.UserResponseDTO;
import com.uem.sd.trabalho.user.model.User;
import com.uem.sd.trabalho.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableRabbit
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO createUser(final UserRequestDTO userDTO) {
        User user = userRepository.save(new User(null, userDTO.name(), userDTO.email()));
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponseDTO getUser(final Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail()))
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    public List<UserResponseDTO> listUser() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }
    @RabbitListener(queues = "get-user")
    public UserResponseDTO getUserById(Long id) {
        log.info("Getting user with id: " + id.toString());
        return getUser(id);
    }

    @RabbitListener(queues = "user-validation")
    public Boolean validateUser(Long userId) {
        Boolean valid = (Boolean) userRepository.findById(userId).isPresent();
        log.info("Validating user with ID: " + userId.toString() + " valid: " + valid.toString());
        return valid;
    }
}
