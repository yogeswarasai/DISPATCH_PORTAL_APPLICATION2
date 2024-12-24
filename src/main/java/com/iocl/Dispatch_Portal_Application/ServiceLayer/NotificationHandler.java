package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iocl.Dispatch_Portal_Application.modal.Notification;
@Component
public class NotificationHandler extends TextWebSocketHandler {
    private final static Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

@Autowired
private final EmployeeService employeeService;

    public NotificationHandler(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String queryString = session.getUri().getQuery();
        String[] queryParams = queryString.split("&");
        String jwt = null;
        String userId = null;
        for (String param : queryParams) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals("jwt")) {
                jwt = keyValue[1];
            } else if (keyValue[0].equals("userId")) {
                userId = keyValue[1];
            }
        }
        System.out.println("JWT TOKEN + " + jwt);
        System.out.println("USER ID =" + userId);
        System.out.println("boolean check "+employeeService.validateJwtToken(jwt));
        if(employeeService.validateJwtToken(jwt)) {
            System.out.println("connection successfull" + userId);
            sessions.put(userId, session);
            System.out.println("printing default session" + sessions.get(userId));
        }
        else{
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // handle incoming messages if needed
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        sessions.remove(userId);
    }

    private String getUserIdFromSession(WebSocketSession session) {
        return session.getUri().getQuery().replace("userId=", "");
    }

    public static void sendNotification(String userId, Notification notification) {
        System.out.println("PRinitng all sessiosn" + sessions);
        System.out.println("Send notif"+userId);
        WebSocketSession session = sessions.get(userId);
        System.out.println("session "+session);
        if (session != null && session.isOpen()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(notification);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                // handle exception if needed
            }
        }
    }
}
