package dev.ticketManager.services;

import dev.ticketManager.entities.User;
import dev.ticketManager.repositories.user.UserRepository;
import dev.ticketManager.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<UserDto> createNewUser(User user) {
        return userRepository.findByLogin(user)
                .flatMap(exists -> {
                    if (exists) {
                        log.info("Пользователь с логином '{}' уже существует", user.getLogin());
                        return Mono.error(new RuntimeException("Пользователь уже существует"));
                    }
                    log.debug("Создание нового пользователя с логином '{}'", user.getLogin());
                    return userRepository.createNewUser(user)
                            .doOnError(e -> log.error("Ошибка при создании пользователя '{}': {}", user.getLogin(), e.getMessage()));
                });
    }
}
