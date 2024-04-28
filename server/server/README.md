## Server Side Messaging System
This is a simple server side messaging system that allows users to send messages to each other.

### Evolution of the system
This system has had countless iterations and refactors (as it is server side, only final version is shown in the repo).
- Stage 1: Unicast messaging system (one-to-one messaging) without chat distinction, it was implemented with Typescript 
for testing purposes, but I consider this project to be a little more serious.
- Stage 2: Multicast messaging system (one-to-many messaging) with chat distinction, implemented with Java. In this stage 
there was a chat distinction, but the messages were not stored, so if a user connected to the chat later, he would not be 
able to see the previous messages. Next stage would require a storing system, I did not really consider setting up Maven or Gradle
on the server.
- Stage 3 (Final): Multicast messaging system with chat distinction and message storing, implemented with Rust. This is the final version.
This is approach is rather naive/stupid as it stores messages in JSON files (one JSON file for each chat), but it is a simple solution that works.