package com.baizhi.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
//访问服务端的url地址
@ServerEndpoint(value = "/websocket")
public class WebSocketServer {
    private static List<Session> sessions = new CopyOnWriteArrayList<Session>();
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("this:" + this);
        sessions.add(session);
        this.session = session;
        session.getBasicRemote().sendText("欢迎连接，服务器...");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void sendtoAll(String message) throws IOException {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
