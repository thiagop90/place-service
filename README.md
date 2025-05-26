<h1 align="center">
  Place Service
</h1>

API para gerenciar lugares (CRUD) para pessoas desenvolvedoras backend que se candidatam para a ClickBus.

## Tecnologias

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Webflux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring Data + R2DBC](https://docs.spring.io/spring-framework/reference/data-access/r2dbc.html)
- [SpringDoc OpenAPI 3](https://springdoc.org/v2/#spring-webflux-support)
- [Slugify](https://github.com/slugify/slugify)

## Práticas adotadas

- SOLID
- Testes automatizados
- Consultas com filtros dinâmicos usando o Query By Example
- API reativa na web e na camada de banco
- Uso de DTOs para a API
- Injeção de Dependências
- Geração automática do Swagger com a OpenAPI 3
- Geração de slugs automática com o Slugify
- Auditoria sobre criação e atualização da entidade

## Como Executar

### Localmente
- Clonar repositório git
- Construir o projeto:
```
./mvnw clean package
```
- Executar:
```
java -jar place-service/target/place-service-0.0.1-SNAPSHOT.jar
```

A API poderá ser acessada em [localhost:8080](http://localhost:8080).
O Swagger poderá ser visualizado em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Usando Docker

- Clonar repositório git
- Construir o projeto:
```
./mvnw clean package
```
- Construir a imagem:
```
./mvnw spring-boot:build-image
```
- Executar o container:
```
docker run --name place-service -p 8080:8080  -d place-service:0.0.1-SNAPSHOT
```

A API poderá ser acessada em [localhost:8080](http://localhost:8080).
O Swagger poderá ser visualizado em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [httpie](https://httpie.io):

- POST /places
```
http POST :8080/places name="Place" city="City" state="State"

HTTP/1.1 201 Created
Content-Length: 149
Content-Type: application/json

{
    "createdAt": "2025-05-26T21:33:20.22415",
    "name": "Place",
    "city": "City"
    "slug": "place",
    "state": "State",
    "updatedAt": "2025-05-26T21:33:20.22415"
}
```

- GET /places/{id}
```
http GET :8080/places/1
HTTP/1.1 200 OK
Content-Length: 143
Content-Type: application/json

{
    "createdAt": "2025-05-26T21:33:20.22415",
    "name": "Place",
    "city": "City"
    "slug": "place",
    "state": "State",
    "updatedAt": "2025-05-26T21:33:20.22415"
} 
```

- GET /places?name=?
```
http GET :8080/places name==PLACE
HTTP/1.1 200 OK
Content-Type: application/json
transfer-encoding: chunked

[
    {
        "createdAt": "2025-05-26T21:33:20.22415",
        "name": "Place",
        "city": "City"
        "slug": "place",
        "state": "State",
        "updatedAt": "2025-05-26T21:33:20.22415"
    }
]
```

- PATCH /places/{id}
```
http PATCH :8080/places/1 name='New Name' city='New City' state='New State'
HTTP/1.1 200 OK
Content-Length: 144
Content-Type: application/json

{
    "createdAt": "2025-05-26T21:33:20.22415",
    "name": "New Name",
    "city": "New City"
    "slug": "new-name",
    "state": "New State",
    "updatedAt": "2025-05-26T21:45:14.69592788"
}
```