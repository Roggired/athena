## Messenger v.0.1.0

## Version Notes

## Requirements

- Openjdk 11
- Docker

## Usage

Firstly, check that you have all requirements installed.

If not:
```bash
sudo apt-get install docker \
&& curl -s "https://get.sdkman.io" | bash \
&& source "$HOME/.sdkman/bin/sdkman-init.sh" \
&& sdk install java 11.0.2-open
```

----

Installation (clear project):
0. Give run permissions to all sh scripts:
```bash
chmod u+x *.sh db/*.sh
```
1. Check that your port 5432 is free:
```bash
sudo netstat -tulp | grep 5432
```
2. Create and run a docker container with messenger database:
```bash
./db/create-db.sh
```
2. Init database using Liquibase:
```bash
./gradlew db:update
```

----

Start (installed project):
1. Run docker container with messenger database:
```bash
./db/start-db.sh
```
2. If you change a database or pull someone changes which may change a database, you need to update
database using Liquibase:
```bash
./gradlew db:update
```
3. Build the project:
```bash
./gradlew build
```
4. Run services which you need in separate terminals:
```bash
java -jar MODULE_NAME/build/libs/MODULE_NAME-VERSION.war
```
