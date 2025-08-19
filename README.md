## Описание
Сервис для управления билетами с использованием Spring WebFlux и R2DBC.  
Поддерживает регистрацию пользователей, аутентификацию через JWT (access и refresh токены) и операции с билетами.

## Основные функции
- CRUD билетов
- Регистрация и аутентификация пользователей
- JWT авторизация (access и refresh токены)
- Swagger документация для всех REST запросов
- Валидация входных данных с кастомным форматом ошибок
- Интеграция с PostgreSQL и автоматическое управление схемой через Flyway

## Стек технологий
- Java 17
- Spring Boot 3.5.4
- Spring WebFlux
- Spring Security с JWT
- R2DBC + PostgreSQL
- Flyway
- Maven
- Docker + Docker Compose
- Swagger (OpenAPI)
