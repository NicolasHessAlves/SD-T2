package com.uem.sd.trabalho.user.controller;

import com.uem.sd.trabalho.user.dto.UserRequestDTO;
import com.uem.sd.trabalho.user.dto.UserResponseDTO;
import com.uem.sd.trabalho.user.service.UserService;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    ResponseEntity<UserResponseDTO> createUser(@RequestBody final @Valid UserRequestDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") final Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<UserResponseDTO>> listUser() {
        return new ResponseEntity<>(userService.listUser(), HttpStatus.OK);
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
