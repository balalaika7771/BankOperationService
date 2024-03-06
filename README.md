[![CI](https://github.com/balalaika7771/BankOperationService/actions/workflows/build.yml/badge.svg)](https://github.com/balalaika7771/BankOperationService/actions/workflows/build.yml)
# Bank Operation Service

This project is a banking application developed using Spring Boot 3 and Java 17. It provides various endpoints for managing bank operations such as transfers, account management, and user information.
[quest](quest.md)
## Features

- Transfer money between accounts
- Manage user accounts
- Add and remove email/phone contacts
- View account information

## Technologies Used

- Spring Boot 3
- Java 17
- PostgreSQL
- Docker
- Maven

## Getting Started

To run this project locally, you need to have Docker installed on your machine. Follow these steps:

1. Clone the repository:

```bash
git clone https://github.com/balalaika7771/BankOperationService.git
```

2. Navigate to the project directory:

```bash
cd bank-operation-service
```

3. Start the PostgreSQL container:

```bash
docker-compose up -d
```

4. Build and run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The application will be accessible at `http://localhost:8080`.

## API Documentation

The API documentation is generated using OpenAPI. You can access it at `http://localhost:8080/swagger-ui/index.html`.

## Maven Documentation

You can generate the Maven documentation by running:

```bash
./mvnw site
```

The documentation will be available in the `target/site` directory.

## Contributing

Contributions are welcome! Feel free to submit pull requests or open issues for any improvements or feature requests.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
