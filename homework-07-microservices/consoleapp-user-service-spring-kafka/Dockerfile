FROM eclipse-temurin:17-jdk-jammy

# Создаем директорию для приложения
WORKDIR /app

# Копируем jar файл (учитывайте, что имя может отличаться)
COPY target/*.jar app.jar

# Порт для приложения (будет случайный, но указываем для документации)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]