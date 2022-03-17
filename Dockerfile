FROM openjdk:11
ARG JAR_FILE="build/libs/messenger-0.0.1.jar"
RUN mkdir /deployments/
COPY ${JAR_FILE} "/deployments/app.jar"
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/deployments/app.jar"]