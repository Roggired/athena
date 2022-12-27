## Versions Notes

v0.3.3:
- fix issues connected with WebSocket API. Now WebSocket API is fully available with new authentication schema
- refactor ws-api module in messenger service. Remove redundant abstractions, simplify subscription logic to keep it
as simple as possible

v0.3.2:
- **issue#60** Add request sign-up function
- **issue#64** Add request sign-in function

v0.3.1:
- minor improvements on admin-panel website

v0.3.0:
- auth service has been rewritten. Admin service has been integrated into auth service:
  - **issue#50** New user model
  - **issue#56** New admin authentication
  - **issue#57** New user authentication
  - **issue#58** New admin registration
  - **issue#59** Temporary password for admin account (not implemented in admin panel website)
  - **issue#61** User account creation by admin manually
  - **issue#62** New invitation for user triggered by admin manually
  - **issue#63** User lock and unlock with lock reason (lock reason not implemented in admin panel website)
  - **issue#65** If user uses invalid refreshToken, his account will be locked automatically
  - **issue#70** New design for admin panel website
- messenger service has been refactored dut to changes in auth service:
  - **issue#72** Messenger service has been refactoring. Now it is using new auth REST API (currently, user's names are not supported)
- admin service and respective gradle module has been removed
- Postgres and Redis docker containers for dev environment now creating via docker-compose. Liquibase migrations have been moved from 
db gradle module to messenger and auth modules. DB gradle module has been removed
- add CI\CD pipeline for unit and functional testing. Add stage environment in RuVDS
- Jasypt encryption has been removed

v0.2.3:
- **issue#41** fix bug with LocalDateTime parsing on MacOC

v0.2.2:
- **issue#37** fix bug with creating personal chats
- **issue#39** fix bug with chat paging
- **issue#42** make new view for /api/v1/users/authorize

v0.2.1:
- **issue#35** fix endpoint /api/v1/chats
- **issue#33** fix endpoint /api/v1/chats/1/messages/messages
- **issue#34** remove this user from page returning on /api/v1/userProfiles
- **issue#36** add endpoint for bulk users registration to admin service

v.0.2.0 **ATTENTION! v.0.2.0 MAY NOT BE COMPATIBLE WITH v.0.1.x**:
- **issue#21** Pagination for users, chats and messages in Messenger
  - Now messages cannot be obtained with a chat. Endpoint GET /fullView has been changed to GET /messages with pagination
  - Endpoints GET /users GET /chats has been updated and now require pagination
- **issue#23** View messages
  - Add new WS notification: VIEWED_MESSAGES
- **issue#24** Is online status for users. Is online status is implemented by active websockets. Therefore, all users
  for whom a websocket is open, will be marked as online. Besides, when a websocket is opened for a user, this user will
  be updated with "lastOnlineTime" equal to now.
- **issue#18** Pin messages
  - Add entity "topic" and CRUD API for that
  - Alter message model - add "topic" and "isPinned"
  - Add endpoints for pinning and unpinning messages and for assigning a topic for the message
  - Add new WS notifications: CHANGED_TOPIC, PIN_MESSAGE, UNPIN_MESSAGE, DELETED_TOPICS
- **issue#27** Search messages by topic with pagination 
- **issue#16** Group chats
  - Alter entity "chat" - add field "type" (possible values: PERSONAL, GROUP)
  - Alter endpoint "createChat" - now only long "userId" is needed for the request
  - Add endpoint "createGroupChat"
  - Add invitations to group chats. The invitation has "id", "sender", "chat"
  - Add endpoints "inviteUser", "acceptInvitation", "leaveChat", "getAllMineInvitations"
  - Add new WS notifications: NEW_INVITATION, NEW_USER, LEAVED_USER
- **issue#28** Add "targetChatId" to notification model. This field is useful for finding the chat to which this 
notification belongs to. I think, it would be more efficient to find the chat in mobile app's cache rather than 
search for messageId in each chat. See [WebSocket API Reference](WebSocket%20API.md) for details.

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