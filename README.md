# Plazoleta Microservice

This is a microservice called "Plazoleta" developed with Java 17, Spring Boot, Gradle, and MySQL. The microservice is designed to manage restaurants and orders in a food court.

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
* ![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)


## Requirements

Make sure you have the following installed before running the microservice:

* JDK 17 [https://jdk.java.net/java-se-ri/17](https://jdk.java.net/java-se-ri/17)
* Gradle [https://gradle.org/install/](https://gradle.org/install/)
* MySQL [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/)

### Recommended Tools
* IntelliJ Community [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
* Postman [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

### Installation

## Configuration

1. Clone the microservice repository:

   ```shell
   git clone https://github.com/rodr15/pragma-powerup-plazoletamicroservice.git
   ```
   
2. Configure the MySQL database:
   * Create a database named plazoleta.
   ```sh
   cd plazoletamicroservice
   ```
   * Update the database connection settings
   
   ```yml
   # src/main/resources/application-dev.yml
   spring:
      datasource:
          url: jdbc:mysql://localhost/plazoleta
          username: root
          password: <your-password>
   ```
   * Insert the data in the BD given in 
   ```yml
   # src/main/resources/data.sql
   ```
3. Configure the Twillio API KEY:
    ```yml
   # src/main/resources/application-dev.yml
   twillio:
      accountSid:
      authToken: 
      fromPhone: 
   ```

<!-- USAGE -->
## Usage
Right-click the class PowerUpApplication and choose Run

## Autenticación y Roles

Para acceder a ciertos endpoints de la API, es necesario autenticarse con un rol determinado. A continuación se detallan los roles y los endpoints asociados:

- `ROLE_ADMIN`: Permite acceder a los siguientes endpoints:
   - `/restaurant/add` (POST)
  
- `ROLE_OWNER`: Permite acceder a los siguientes endpoints:
  - `/dish/add` (POST)
  - `/dish/update/{dishId}` (PUT)
  - `/dish/enable-disable/{dishId}/{dishState}` (PUT)

- `ROLE_EMPLOYEE`: Permite acceder a los siguientes endpoints:
  - `/restaurant/order-list` (GET)
  - `/restaurant/assign-order` (PUT)

- `ROLE_CLIENT`: Permite acceder a los siguientes endpoints:
  - `/restaurant/restaurant-list` (GET)
  - `/restaurant/{restaurantId}/menu` (GET)
  - `/restaurant/order` (POST)

## Documentation
The microservice documentation is done using Swagger. Once the microservice is up and running, you can access the documentation through the following URL:
```sh
http://localhost:8091/swagger-ui.html
```

<!-- ROADMAP -->
## Tests

- Right-click the test folder and choose Run tests with coverage
# pragma-powerup-plazoletamicroservice
