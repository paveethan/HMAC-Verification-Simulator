## Synopsis

This program was written for the course MTH-816 (Cryptography), and is a simulatation program that shows HMAC verification in process. It consists of a client application and a server application, and the client application sends a message to the server application which utilizes HMAC to ensure the authenicity of the message, and there exists a mode in which the user can send corrupted message (messages that have been altered or tampered with, by allowing the user to alter the message's original content, and the hashed value appended to the message).

## Notes

It was written as a proof of concept, and it is discouraged from utilizing in the real-world without further modifications.

## Running & Compiling Instructions

In order to run the application, please run the following commands in sequence:

(skip if already compiled)
- Ensure you're in correct directory
- "javac server.java"
- "javac guiClient.java"

then first run the server
- "java server <portNumber>" where <portNumber> is a valid port number 1025-49151
i.e "java server 4444"

then run client
- "java guiClient <hostName> <portNumber>" where <hostName> is the hostname or IP of the server 
	(if using on same machine then localhost), and <portNumber> is the same portNumber
	the server is using
i.e "java guiClient localhost 4444"

Then the application will run correctly.