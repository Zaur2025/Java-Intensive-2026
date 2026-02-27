# Домашнее задание 4: User Service на Spring Boot.

## REST API для управления пользователями

### Эндпоинты:
- `POST /api/users` - создать пользователя
- `GET /api/users` - получить всех пользователей
- `GET /api/users/{id}` - получить пользователя по ID
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя

### Запуск PostgreSQL в Docker:
docker run --name userservice -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1qaz!QAZ -e POSTGRES_DB=userdb -p 5432:5432 -d postgres:15

### Запуск приложения:
```bash
mvn spring-boot:run