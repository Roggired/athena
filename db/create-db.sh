docker run -d --name yofik-messenger-db -p 5432:5432 -e POSTGRES_DB=messenger -e POSTGRES_USER=messenger -e POSTGRES_PASSWORD=messenger postgres:latest
