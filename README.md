1. Install and start PostgreSQL 16 database. Execute data.sql.
2. Run the application from IntelliJ IDEA (or similar IDE) through DreamDiaryApplication class, which serves as entry point.
   **Alternatively**, you can run it from terminal by executing `gradlew bootRun` while in project's root directory.
3. Login through web browser's Login page, or through Postman. In the case of the latter, create POST request to `localhost:8080/api/v1/auth/login`, with following request body:
{
    "username": "omori",
    "password": "password"
}
or any other valid credentials, as defined in `users` database. The server's response body will contain JWT Bearer token you should use on subsequent Postman requests.
4. You can now access other routes. If logged in as admin, you can also access admin only routes. 
