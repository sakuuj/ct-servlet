# Tasks servlets
### Инструкция по запуску
1. `./gradlew :war`
2. `docker compose up`

### Servlet endpoints
* GET `http://localhost:8080/clients`
* GET `http://localhost:8080/clients/{id}`
* POST `http://localhost:8080/clients` + application/json body [ClientRequest](src/main/java/by/sakujj/dto/ClientRequest.java)

### Filters
* Ловящий исключения [(исходный код)](src/main/java/by/sakujj/servlet/filter/ExceptionHandlerFilter.java)
* Меняющий кодировку ответа на 'utf-8' [(исходный код)](src/main/java/by/sakujj/servlet/filter/EncodingFilter.java)
* Выставляющий заголовок ответа 'Content-Type: application/json'  [(исходный код)](src/main/java/by/sakujj/servlet/filter/ContentTypeFilter.java)