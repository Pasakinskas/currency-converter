## Startup

This project has a React frontend and two Spring Boot services: currency converter and a cron-like scheduler service.

The correct way to start the application is to start the Postgres database with Docker, start currency converter,
then cron-service (on init it will fill currency-converter with data) and finally the frontend

`docker-compose up db`

Run or build spring boot project with your IDE `backend/currency-converter`

Run or build spring boot project with your IDE `backend/cron-scheduler`

`npm start` in `frontend` directory


## Tests

React frontend and Spring boot currency-converter are covered with unit tests

to run React tests
```
cd frontend
npm start
```

Run the backend tests with your IDE on the project `backend/currency-converter`

Building and running tests works with IntelliJ, might need additional config with other IDEs.