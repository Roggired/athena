FROM openjdk:11

COPY build/libs/messenger-0.1.2.war /app/app.war
COPY certs/Server-keystore.p12 /app/
COPY certs/Server-truststore.p12 /app/

WORKDIR /app

CMD ["java", "-jar", "-Dspring.profiles.active=dev", "app.war"]