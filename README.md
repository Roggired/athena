## Messenger v.0.1.0

## Version Notes
v.0.1.0
- Auth, Admin, Messenger services which implements only text messages functionalities
- Sending messages via HTTPS request
- Receiving messages via single notification websocket

## Requirements

- Openjdk 11
- Docker
- Postman (for testing REST API)
- Jasypt 1.9.3

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
3. For development on localhost you need to update `/etc/hosts`:
```bash
127.0.0.1 yofik.messenger.auth
```
4. Create SSL certificates for local HTTPS requests:
```bash
mkdir ~/.cert
mkdir ~/.cert/yofik-messenger/
cp generate-certs.sh ~/.cert/yofik-messenger/
cd ~/.cert
./generate-certs.sh
cd PROJECT_ROOT
cd auth/
mkdir certs
cp ~/.cert/yofik-messenger/Server-keystore.p12 certs
cp ~/.cert/yofik-messenger/Server-truststore.p12 certs
```
5. Provide clientJpaDto certs to the clientJpaDto (maybe Postman File->Settings->Certificates):
```bash
~/.cert/yofik-messenger/CA-self-signed-certificate.pem
~/.cert/yofik-messenger/Client-certificate.crt
~/.cert/yofik-messenger/Client-private-key.key
```
6. If you want, you can change `yofik.security.jwe-key` option in `application-dev.yml`:
- Download and unzip Jasypt dist (https://github.com/jasypt/jasypt/releases/tag/jasypt-1.9.3)
- Generate random jwe-key like byte array (at least 256 bytes) and encode it with `Base64` 
or use `/api/v1/clients/key` to generate such key
```bash
cd JASYPT_INSTALLATION/bin
chmod u+x encrypt.sh
./encrypt.sh input="NEW_BASE_JWE_KEY" password=JASYPT_PASSWORD algorithm=PBEWithMD5AndDES
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
cd SERVICE_NAME
java -jar -Djasypt.encryptor.password=supersecretz build/libs/SERVICE_NAME-VERSION.war
```
Also, to provide Jasypt password (for IDEA configuration or production build) you can set up the environment
variable JASYPT_ENCRYPTOR_PASSWORD

## SSL note

If you want, you can easily disable SSL for messenger and admin services (web interface). To do so, you need 
to create directories: `messenger/config` and `admin/config`. After that copy files `application-dev.yml` from 
each module and place in associated config directory and disable ssl.

## API reference

REST api docs can be found on `http://localhost:8080/swagger-ui.html`

WebSocket api is described below in `WebSocket API` section

## WebSocket API

`ws://localhost:8080/ws-api/v1/notifications` - notification websocket.
Subscribe message:
```json
{
  "command": "SUBSCRIBE_ON_NOTIFICATIONS"
}
```

Messenger service will send a message for each message to user this socket is associated to:
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": "<MessageView as JSON here. See ru.yofik.athena.messenger.context.chat.view.MessageView> Example: {id:26,text:Hello, world!,senderId:14,chatId:19,date:2022-04-09T16:06:25.998397} "
}
```

NOTE:
1. To connect to the server via websocket, a client sends special HTTP request (handshake). As soon as this
request is HTTP, we can use the `Authorization` header to secure websocket endpoints. So, when you are trying to 
connect, you need to specify `Authorization` header as below:
`Authorization: Bearer <client-token> <access-token>`
2. After the connection is established, Messenger service will not expect any authentication information as soon as 
websockets are long-lived connections.

