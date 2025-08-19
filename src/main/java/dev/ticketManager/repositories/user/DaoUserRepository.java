package dev.ticketManager.repositories.user;

import dev.ticketManager.entities.User;
import dev.ticketManager.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DaoUserRepository implements UserRepository {
    private final DatabaseClient databaseClient;

    private static final String SAVE_NEW_USER = """
            insert into user_data(login, password, fio)
            values(:login,:password,:fio)
            returning user_data_id,login, fio
            """;

    private static final String FIND_BY_LOGIN = """
            select ud.login
            from user_data ud
            where ud.login = :login
            """;

    @Override
    public Mono<UserDto> createNewUser(User user) {
        log.debug("Запрос к БД на создание нового пользователя с логином '{}'", user.getLogin());
        log.trace("SQL запрос: {}", SAVE_NEW_USER);
        return databaseClient.sql(SAVE_NEW_USER)
                .bind("login", user.getLogin())
                .bind("password", user.getPassword())
                .bind("fio", user.getFio())
                .map((row, meta) -> new UserDto(
                        row.get("login", String.class),
                        row.get("fio", String.class)
                ))
                .one();
    }


    @Override
    public Mono<Boolean> findByLogin(User user) {
        log.debug("Запрос к БД на получения похожего логина '{}'", user.getLogin());
        log.trace("SQL запрос: {}", FIND_BY_LOGIN);
        return databaseClient.sql(FIND_BY_LOGIN)
                .bind("login", user.getLogin())
                .fetch()
                .one()
                .map(r -> true)
                .defaultIfEmpty(false);
    }
}
