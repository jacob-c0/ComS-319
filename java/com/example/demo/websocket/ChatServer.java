package com.example.demo.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 *
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 *
 * Example URL: ws://localhost:8080/chat/username
 *
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 */
@ServerEndpoint("/chat/{username}")
@Component
public class ChatServer {

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();
    private static Map <Session, Session > userChattingWith = new Hashtable<>();
    private List<String> inactiveUsers = new ArrayList<String>();

    private ObjectMapper objectMapper = new ObjectMapper();
    // server side logger
    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        // server side log
        logger.info("[onOpen] " + username);

        //removes from inactive users if exists
        inactiveUsers.remove(username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        }
        else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);


            // send to the user joining in
            sendMessageToPArticularUser(session,username, "Click a username to begin private chat, "+username);

            // send to everyone in the chat
            newUser();
        }

    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        //convert json to readable class
        RecievedData data = objectMapper.readValue(message,RecievedData.class);
        String username = sessionUsernameMap.get(session);
        if (data.type.equals("userClick"))
        {
            userChattingWith.put(session,usernameSessionMap.get(data.content));
            logger.info(username + " clicked " + data.content);
        }
        else if (data.type.equals("message")) {

            // server side log
            logger.info("[onMessage] " + username + ": " + data.content);

                if (userChattingWith.containsKey(session))
                {
                    sendMessageToPArticularUser(session,sessionUsernameMap.get(userChattingWith.get(session)), data.content);
                }
                else
                {
                    sendMessageToPArticularUser(session,sessionUsernameMap.get(session),"Please click a user to begin chatting with");
                }

        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        inactiveUsers.add(username);

        newUser();
    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToPArticularUser(Session session, String username, String message) {
        try {
            Message m = new Message();
            m.type = "message";
            if (username.equals(sessionUsernameMap.get(session)))
            {
                m.message = "[From Server]: " + message;
            }
            else
            {
                m.message = "[From " + sessionUsernameMap.get(session) + "]: " + message;
            }
            String finalMessage = objectMapper.writeValueAsString(m);
            usernameSessionMap.get(username).getBasicRemote().sendText(finalMessage);
            if (!username.equals(sessionUsernameMap.get(session))) {
                m.message = "[To " + username +  "]: " + message;
                finalMessage = objectMapper.writeValueAsString(m);
                session.getBasicRemote().sendText(finalMessage);
            }
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                Message m = new Message();
                m.type = "message";
                m.message = message;
                session.getBasicRemote().sendText(objectMapper.writeValueAsString(m));
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

    private void newUser()
    {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                Message m = new Message();
                m.type = "userUpdate";
                m.users = usernameSessionMap.keySet().toArray(new String[0]);
                m.inactiveUsers = inactiveUsers.toArray(new String[0]);
                session.getBasicRemote().sendText(objectMapper.writeValueAsString(m));
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }

    private class Message {
        public String type;
        public String[] users;
        public String[] inactiveUsers;
        public String message;

        public Message()
        {

        }
    }

    private static class RecievedData
    {
        public String type;

        public String content;
    }



}