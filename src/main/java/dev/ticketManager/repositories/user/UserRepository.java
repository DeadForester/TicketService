package dev.ticketManager.repositories.user;

import dev.ticketManager.entities.User;
import dev.ticketManager.web.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<UserDto> createNewUser(User user);

    Mono<Boolean> findByLogin(User user);
}
