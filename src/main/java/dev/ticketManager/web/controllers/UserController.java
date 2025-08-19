package dev.ticketManager.web.controllers;

import dev.ticketManager.entities.User;
import dev.ticketManager.services.UserService;
import dev.ticketManager.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя, если логин ещё не занят")
    public Mono<ResponseEntity<UserDto>> createNewUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя с логином '{}'", user.getLogin());
        return userService.createNewUser(user)
                .map(userDto -> {
                    log.info("Пользователь '{}' успешно создан", userDto.getLogin());
                    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
                })
                .doOnError(e ->
                        log.warn("Не удалось создать пользователя '{}'. Причина: {}", user.getLogin(), e.getMessage()));
    }

}

