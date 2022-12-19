FROM openjdk:11

COPY build/libs/auth-0.3.0.war /app/app.war
COPY certs/Server-keystore.p12 /app/
COPY certs/Server-truststore.p12 /app/

WORKDIR /app

CMD ["java", "-jar", "-Dspring.profiles.active=dev", "app.war"]