package com.uem.sd.trabalho.to_do_list.controller;

import com.uem.sd.trabalho.to_do_list.dto.ToDoRequestDTO;
import com.uem.sd.trabalho.to_do_list.dto.ToDoResponseDTO;
import com.uem.sd.trabalho.to_do_list.service.ToDoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("to-do")
@RequiredArgsConstructor
public class ToDoController {
    private final ToDoService toDoService;
    @PostMapping("/{userId}")
    ResponseEntity<ToDoResponseDTO> createToDo(@RequestBody final @Valid ToDoRequestDTO toDoDTO, @PathVariable("userId") final Long userId) {
        return new ResponseEntity<>(toDoService.createToDo(toDoDTO, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<ToDoResponseDTO> updateToDo(@RequestBody final @Valid ToDoRequestDTO toDoDTO, @PathVariable("id") final Long userId) {
        return new ResponseEntity<>(toDoService.updateToDo(toDoDTO, userId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ToDoResponseDTO> getToDo(@PathVariable("id") final Long id) {
        return new ResponseEntity<>(toDoService.getToDo(id), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<ToDoResponseDTO>> listToDo() {
        return new ResponseEntity<>(toDoService.listToDo(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<ToDoResponseDTO>> listToDoFromUser(@PathVariable("userId") final Long userId) {
        return new ResponseEntity<>(toDoService.listToDoFromUser(userId), HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields.");
        problemDetail.setTitle("Invalid input");
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("path", request.getDescription(false));

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }
}
