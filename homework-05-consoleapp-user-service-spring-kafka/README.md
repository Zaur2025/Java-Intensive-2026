# Домашнее задание 5: Реализация микросервиса(notification-service) для отправки сообщения на почту при удалении или добавлении пользователя.

### Запуск PostgreSQL в Docker:
docker run --name userservice -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1qaz!QAZ -e POSTGRES_DB=userdb -p 5432:5432 -d postgres:15

### Запуск приложения:
```bash
mvn spring-boot:run