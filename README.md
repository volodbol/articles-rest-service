# articles-rest-service

Task description [here](task-description.txt)

To run application you need to execute this command in a root folder of project
(you need to have installed java with system environments set)

```
mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

To access OpenAPI 3.0 (Swagger) use this link `http://localhost:8080/swagger-ui.html`

To import collecting to Postman use this link `http://localhost:8080/v3/api-docs`

Pay attention that application has JWT security. First of all sent a http post to
`/api/v1/auth/authenticate` with this body

```
{
    "username": "admin",
    "password": "admin"
}
```

Receive token

```
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4MDg3ODI2MCwiZXhwIjoxNjgwOTY0NjYwfQ.bcNLp7eeI4q8g5Z-glG4-XVpwbLgH4felfLk7_ceKFc",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4MDg3ODI2MCwiZXhwIjoxNjgxNDgzMDYwfQ.u85h1GtcX2KPdLqhA0pi0JflDSRj3YhITwKn3gEfU1I"
}
```

And then just sent requests with header `Authentication` and value

```
Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4MDg3ODI2MCwiZXhwIjoxNjgwOTY0NjYwfQ.bcNLp7eeI4q8g5Z-glG4-XVpwbLgH4felfLk7_ceKFc
```