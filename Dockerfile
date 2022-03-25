FROM openjdk:11
ARG JAR_FILE="build/libs/messenger-0.1.0.war"
RUN mkdir /deployments/
COPY ${JAR_FILE} "/deployments/app.war"
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/deployments/app.war"]