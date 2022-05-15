## WebSocket API Reference

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
    "targetChatId": 57,
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
    "targetChatId": 57,
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
    "targetChatId": 57,
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

**Changed topic** - when a message has changed its topic. Changing the topic do not affect modificationDate
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "CHANGED_TOPIC",
    "targetChatId": 46,
    "payload": {
      "initiatorId": 2,
      "message": {
        "id": 52,
        "text": "Халлоу, бойз!!!",
        "senderId": 2,
        "chatId": 46,
        "creationDate": "2022-05-15T13:27:50.116470",
        "modificationDate": "2022-05-15T13:27:50.116470",
        "viewedByUserIds": [
          2
        ],
        "topic": {
          "id": 53,
          "name": "Важное"
        },
        "isPinned": false
      }
    }
  }
}
```

**Pin message** - when any user pins a message (the topic also may be changed). Pinning do not affect modificationDate
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "PIN_MESSAGE",
    "targetChatId": 46,
    "payload": {
      "initiatorId": 2,
      "message": {
        "id": 52,
        "text": "Халлоу, бойз!!!",
        "senderId": 2,
        "chatId": 46,
        "creationDate": "2022-05-15T13:27:50.116470",
        "modificationDate": "2022-05-15T13:27:50.116470",
        "viewedByUserIds": [
          2
        ],
        "topic": {
          "id": 53,
          "name": "Важное"
        },
        "isPinned": true
      }
    }
  }
}
```

**Unpin message** - when any user unpins a message (the topic cannot be changed). Unpinning do not affect modificationDate
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "UNPIN_MESSAGE",
    "targetChatId": 46,
    "payload": {
      "initiatorId": 2,
      "message": {
        "id": 52,
        "text": "Халлоу, бойз!!!",
        "senderId": 2,
        "chatId": 46,
        "creationDate": "2022-05-15T13:27:50.116470",
        "modificationDate": "2022-05-15T13:27:50.116470",
        "viewedByUserIds": [
          2
        ],
        "topic": {
          "id": 53,
          "name": "Важное"
        },
        "isPinned": false
      }
    }
  }
}
```

**Deleted topic** - when a topic is deleted
```json
{
  "command": "RECEIVE_NOTIFICATION",
  "argument": {
    "type": "DELETED_TOPICS",
    "targetChatId": 46,
    "payload": [53, 54]
  }
}
```

**New user** - when a user joins a chat
```json
{
   "command": "RECEIVE_NOTIFICATION",
   "argument": {
      "type": "NEW_USER",
      "targetChatId": 5,
      "payload": {
         "user": {
            "id": 1,
            "name": "Иляшик",
            "login": "kisusil",
            "isOnline": true,
            "lastOnlineTime": "2022-05-15T18:14:06.610100"
         },
         "chatId": 5
      }
   }
}
```

**Leaved user** - when a user leaves a chat
```json
{
   "command": "RECEIVE_NOTIFICATION",
   "argument": {
      "type": "LEAVED_USER",
      "targetChatId": 5,
      "payload": {
         "user": {
            "id": 1,
            "name": "Иляшик",
            "login": "kisusil",
            "isOnline": true,
            "lastOnlineTime": "2022-05-15T18:14:06.610100"
         },
         "chatId": 5
      }
   }
}
```

**New invitation** - when the user gets a new invitation
```json
{
   "command": "RECEIVE_NOTIFICATION",
   "argument": {
      "type": "NEW_INVITATION",
      "targetChatId": null,
      "payload": {
         "id": "UUID-here",
         "sender": {
            "id": 1,
            "name": "Иляшик",
            "login": "kisusil",
            "isOnline": true,
            "lastOnlineTime": "2022-05-15T18:14:06.610100"
         },
         "chat": {
            "id": 46,
            "name": "Афина Тим",
            "type": "GROUP",
            "userViews": [
               {
                  "id": 1,
                  "name": "Иляшик",
                  "login": "kisusil",
                  "isOnline": true,
                  "lastOnlineTime": "2022-05-15T18:15:23.253348"
               }
            ],
            "lastMessage": {
               "id": 58,
               "text": "Пивет",
               "senderId": 2,
               "chatId": 46,
               "creationDate": "2022-05-15T14:19:16.414645",
               "modificationDate": "2022-05-15T14:19:16.414645",
               "viewedByUserIds": [
                  2
               ],
               "topic": {
                  "id": 60,
                  "name": "Матеша"
               },
               "isPinned": false
            }
         }
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