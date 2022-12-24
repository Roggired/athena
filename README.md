## Messenger v.0.3.2

## Version Notes

[Version Notes](Version%20notes.md)

## Requirements

- Openjdk 11
- Docker
- Postman (for testing REST API)

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

Start (installed project):
1. Create and run a docker container with messenger database and Redis:
```bash
docker-compose up
```
2. Build the project:
```bash
./gradlew build
```
for Windows:
```bat
gradlew.bat build
```
3. Run services which you need in separate terminals:
```bash
cd SERVICE_NAME
java -jar build/libs/SERVICE_NAME-VERSION.war
```
for Windows:
```bat
cd SERVICE_NAME
java -jar build\libs\SERVICE_NAME-VERSION.war
```

## API reference

### REST API

[REST API Reference](REST%20API.md)

### WebSocket API

[WebSocket API Reference](WebSocket%20API.md)
