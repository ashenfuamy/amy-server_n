package site.ashenstation.app.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    // 存储在线用户的Session：key=userId，value=WebSocketSession（线程安全）
    public static final Map<String, WebSocketSession> ONLINE_SESSIONS = new ConcurrentHashMap<>();

    public WebSocketHandler() {
        super();
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        super.handleBinaryMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String uid = (String) session.getAttributes().get("uid");
        String username = (String) session.getAttributes().get("username");

        ONLINE_SESSIONS.put(userId + ":" + uid, session);
        log.info("用户[{}({})]建立WebSocket连接，当前在线人数：{}", userId, userId, ONLINE_SESSIONS.size());
        // 向客户端发送连接成功消息
        session.sendMessage(new TextMessage("✅ 连接成功！当前用户：" + username));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("handleMessage:" + message.getPayload());
        super.handleMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("handleTextMessage:" + message.getPayload());
        super.handleTextMessage(session, message);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String uid = (String) session.getAttributes().get("uid");
        String username = (String) session.getAttributes().get("username");

        ONLINE_SESSIONS.remove(userId + ":" + uid);
        log.info("用户[{}({})]断开WebSocket连接，当前在线人数：{}", username, userId, ONLINE_SESSIONS.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
}
