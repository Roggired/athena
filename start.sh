./gradlew build
./db/start-db.sh
./gradlew db:update -PrunList=messenger
java -jar auth/build/libs/auth-0.1.0.war