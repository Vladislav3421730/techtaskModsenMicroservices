# techtaskModsenMicroservices
First you need to copy the project locally to your computer<br>
```bash
git clone https://github.com/Vladislav3421730/techtaskModsenMicroservices
```
Then in the root folder run the command<br>
```bash
docker compose up
```
The database and Kafka will start themselves along with the data in the database, you don’t need to start anything else.
Then, through IntelIdea, launch (click on the green triangle) microservices in the following sequence:<br>
```ServerRegistry, AuthService, BookService, LibraryService, ApiGateway```<br>
The program has a division into USER and ADMIN roles<br>
A user with the ADMIN role has access to all endpoints.<br>
A user with the User role has access to the following endpoints:<br>
```bash
http://localhost:8080/auth/login
http://localhost:8080/auth/register
http://localhost:8080/book/get
http://localhost:8080/book/free
http://localhost:8080/book/get/{id}
http://localhost:8080/book/get/{isbn}
http://localhost:8080/library/getStatuses
http://localhost:8080/library/getStatus/{book_status_id}
http://localhost:8080/library/updateStatus
```
Unauthorized user has access to endpoints <br>
```bash
http://localhost:8080/auth/login
http://localhost:8080/auth/register
```
When you log in via ```http://localhost:8080/auth/login``` you are given a token, which must be inserted into the header Authorization in the Bearer token.
## Postman Collection
You can test the api thanks to Postman locally on your computer or by opening it using the link:<br>
```bash
https://solar-trinity-124167.postman.co/workspace/first~3b8ccce8-e323-47dd-a41b-98d3d65f7c8b/collection/29171033-f2a03f26-03ee-4881-9758-d37fa832767b?action=share&source=collection_link&creator=29171033
```
## Swagger Documentation
Documentation for microservice AuthService<br>
```bash
http://localhost:8080/swagger/authorization/swagger-ui/index.html
http://localhost:8080/swagger/authorization/v3/api-docs
```
Documentation for microservice BooksService<br>
```bash
http://localhost:8080/swagger/book/swagger-ui/index.html
http://localhost:8080/swagger/book/v3/api-docs
```
Documentation for microservice LibraryService<br>
```bash
http://localhost:8080/swagger/library/swagger-ui/index.html
http://localhost:8080/swagger/library/v3/api-docs
```
