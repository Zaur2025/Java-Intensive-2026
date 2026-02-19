### Домашнее задание 3: консольное приложение(user-service) c Unit и интеграционными тестами.

### Запуск PostgreSQL в Docker:
docker run --name userservice -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1qaz!QAZ -e POSTGRES_DB=userdb -p 5432:5432 -d postgres:15