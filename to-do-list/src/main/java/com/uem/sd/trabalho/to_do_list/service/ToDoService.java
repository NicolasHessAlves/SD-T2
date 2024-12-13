package com.uem.sd.trabalho.to_do_list.service;

import com.uem.sd.trabalho.to_do_list.dto.NotificationDTO;
import com.uem.sd.trabalho.to_do_list.dto.ToDoRequestDTO;
import com.uem.sd.trabalho.to_do_list.dto.ToDoResponseDTO;
import com.uem.sd.trabalho.to_do_list.model.ToDo;
import com.uem.sd.trabalho.to_do_list.repository.ToDoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ToDoRepository toDoRepository;
    private final UserValidation userValidation;
    private final UserNotification userNotification;

    public ToDoResponseDTO createToDo(final ToDoRequestDTO toDoDTO, final Long userId) {

        if (!userValidation.validateUser(userId))
            throw new EntityNotFoundException("User not found.");

        // Validate userId
        ToDo toDo = toDoRepository.save(new ToDo(null, toDoDTO.description(), userId, false));

        userNotification.sendNotification(new NotificationDTO(toDo.getUserId(), toDo.getDescription(), toDo.isDone()));

        return new ToDoResponseDTO(toDo.getId(), toDo.getDescription(), toDo.isDone());
    }

    public ToDoResponseDTO updateToDo(final ToDoRequestDTO toDoDTO, final Long id) {
        Optional<ToDo> toDoDb = toDoRepository.findById(id);

        if (toDoDb.isEmpty())
            throw new EntityNotFoundException("To-do not present.");

        boolean toDoDone = (toDoDTO.done() == null) ? toDoDb.get().isDone() : toDoDTO.done();

        ToDo toDo = toDoRepository.save(new ToDo(id, toDoDTO.description(), toDoDb.get().getUserId(), toDoDone));

        userNotification.sendNotification(new NotificationDTO(toDo.getUserId(), toDo.getDescription(), toDo.isDone()));

        return new ToDoResponseDTO(toDo.getId(), toDo.getDescription(), toDo.isDone());
    }

    public List<ToDoResponseDTO> listToDo() {
        return toDoRepository.findAll().stream()
                .map(toDo -> new ToDoResponseDTO(toDo.getId(), toDo.getDescription(), toDo.isDone()))
                .collect(Collectors.toList());
    }

    public List<ToDoResponseDTO> listToDoFromUser(final Long userId) {
        return toDoRepository.findAllByUserId(userId).stream()
                .map(toDo -> new ToDoResponseDTO(toDo.getId(), toDo.getDescription(), toDo.isDone()))
                .collect(Collectors.toList());
    }

    public ToDoResponseDTO getToDo(final Long id) {
        return toDoRepository.findById(id)
                .map(toDo -> new ToDoResponseDTO(toDo.getId(), toDo.getDescription(), toDo.isDone()))
                .orElseThrow(() -> new EntityNotFoundException("To-do not found."));
    }
}
