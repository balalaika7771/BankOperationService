[![CI](https://github.com/balalaika7771/BankOperationService/actions/workflows/build.yml/badge.svg)](https://github.com/balalaika7771/BankOperationService/actions/workflows/build.yml)

# BankOperationService

BankOperationService - это простой сервис для управления счётами в банке.

## Требования

- Java 17
- Spring Boot 3.x
- Docker

## Установка

1. Клонируйте репозиторий:

    ```bash
    git clone https://github.com/balalaika7771/BankOperationService.git
    ```

2. Убедитесь, что у вас установлены Java 17 и Docker.

3. Запустите Docker, чтобы развернуть базу данных PostgreSQL:

    ```bash
    docker-compose up -d
    ```

4. Соберите и запустите приложение с помощью Maven:

    ```bash
    mvn clean package
    java -jar target/BankOperationService.jar
    ```

5. После завершения работы приложения, остановите контейнеры Docker:

    ```bash
    down
    ```

## Использование

- Откройте документацию OpenAPI, чтобы узнать о доступных API: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

- Следуйте документации API для выполнения операций в банке.

## Дополнительная информация

- Документация Maven: [docs/index.html](docs/index.html)
