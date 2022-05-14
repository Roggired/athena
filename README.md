## Messenger v.0.1.6

## Version Notes
in progress (v.0.2.0 **ATTENTION! v.0.2.0 MAY NOT BE COMPATIBLE WITH v.0.1.x**):
- **issue#21** Pagination for users, chats and messages in Messenger
  - Now messages cannot be obtained with a chat. Endpoint GET /fullView has been changed to GET /messages with pagination
  - Endpoints GET /users GET /chats has been updated and now require pagination
- **issue#23** View messages

v.0.1.6:
- Architecture refactoring
- WS API improvements. Now WS API can be used for any type of notifications
- Fix possible global message deletion after local one
- **issue#26** User should be able to globally delete only their own messages
- **issue#25** Personal chats name resolution

v.0.1.5:
- **issue#22** Feature update user
- **issue#17** Global and local messages deletion. Deletion endpoints have been changed

v.0.1.4 **ATTENTION! v.0.1.4 IS NOT COMPATIBLE WITH v.0.1.3**:
- **issue#14** Fix removing locks. Now when user is unlocked, the lock is removed
- **issue#15** Fix device id. Now Messenger sends client device id instead of Apache HttpClient
- **issue#19** Deleting several messages
- **issue#20** Updating existed messages. New WebSocket notifications:
    1. Changed format of notification AthenaWSMessage. See WS API section
    2. When a user updates or deletes messages, a notification will be sent. See WS API section
    3. Changed MessageView: date -> creationDate, add field modificationDate

v.0.1.3
- **issue#10** Fix invitation regeneration. Now amount of available usages on admin is correct.
- **issue#11** For dev profile change generator of invitations. Now an invitation is a 3digits code.
- **issue#12** Add filter by involved users for getAllChats endpoint in messenger.
- Add support for ReactJS frontend. However, Thymeleaf and ReactJS conflict each other.

v.0.1.2
- Add RestApi for admin service
- Now authentication cookie in admin service is not secure
- Add Swagger OpenApi v3 for admin service (available on http://localhost:8082/swagger-ui.html)

v.0.1.1
- Fix message order and last message problem
- Improve presentation of AthenaWSMessage. Now argument is pretty formatted

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
for Windows:
```bat
gradlew.bat db:update
```
3. Build the project:
```bash
./gradlew build
```
for Windows:
```bat
gradlew.bat build
```
4. Run services which you need in separate terminals:
```bash
cd SERVICE_NAME
java -jar -Djasypt.encryptor.password=supersecretz build/libs/SERVICE_NAME-VERSION.war
```
for Windows:
```bat
cd SERVICE_NAME
java -jar -Djasypt.encryptor.password=supersecretz build\libs\SERVICE_NAME-VERSION.war
```
Also, to provide Jasypt password (for IDEA configuration or production build) you can set up the environment
variable JASYPT_ENCRYPTOR_PASSWORD

## SSL note

If you want, you can easily disable SSL for messenger and admin services (web interface). To do so, you need 
to create directories: `messenger/config` and `admin/config`. After that copy files `application-dev.yml` from 
each module and place in associated config directory and disable ssl.

## API reference

REST api docs can be found on `http://localhost:8080/swagger-ui.html` for messenger and 
on `http://localhost:8082/swagger-ui.html` for admin

WebSocket api is described below in `WebSocket API` section

## WebSocket API

`ws://localhost:8080/ws-api/v1/notifications` - notification websocket.
Subscribe message:
```json
{
  "command": "SUBSCRIBE_ON_NOTIFICATIONS"
}
```

### Notifications:

**New message** - when any user sends a message 
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "NEW_MESSAGE",
    "payload": {
      "id": 1,
      "text": "Hi!",
      "senderId": 14,
      "chatId": 57,
      "creationDate": "2022-05-03T11:45:31.836541",
      "modificationDate": "2022-05-03T11:45:31.836541"
    }
  }
}
```

**Updated message** - when any user updates a message 
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "UPDATED_MESSAGE",
    "payload": {
      "id": 1,
      "text": "Hi! :)",
      "senderId": 14,
      "chatId": 57,
      "creationDate": "2022-05-03T11:45:31.836541",
      "modificationDate": "2022-05-03T12:01:12.531241"
    }
  }
}
```

**Deleted messages** - when any user deletes one or more messages
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "DELETED_MESSAGES",
    "payload": [
      1,
      3,
      4
    ]
  }
}
```

**Viewed messages** - when any user views one or more messages
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "VIEWED_MESSAGES",
    "payload": {
      "messageIds": [1, 2, 3],
      "viewerId": 13
    }
  }
}
```

NOTE:
1. To connect to the server via websocket, a client sends special HTTP request (handshake). As soon as this
request is HTTP, we can use the `Authorization` header to secure websocket endpoints. So, when you are trying to 
connect, you need to specify `Authorization` header as below:
`Authorization: Bearer <client-token> <access-token>`
2. After the connection is established, Messenger service will not expect any authentication information as soon as 
websockets are long-lived connections.

