## API usage
### Retrieve chat for given ride number
```curl
curl GET "http://localhost:8080/chat/{chat_number}"
```
This returns a json object with the chat messages for the given ride number.
```json
[  
    {
      "message": "Hello",
      "sender": "user1"
    },
    {
      "message": "Hi",
      "sender": "user2"
    }
]
```
Do note that this system is terribly inefficient, and should not be used in production. However, we can
afford this inefficiency for the sake of this project.

#### Possible improvements (out of scope for our time constraints)
- Use a database to store the chat messages (feasible, but would require remote db access, we do not have enough time)
- Use a caching layer to store the chat messages
- Use encryption to secure the chat messages
- Use a more efficient serialization format (e.g. Protocol Buffers)