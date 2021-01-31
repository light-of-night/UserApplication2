package com.baizhi.redis;

import com.baizhi.websocket.WebSocketServer;
import org.springframework.stereotype.Component;

import java.io.IOException;

//创建 监听 后的 reseive 方法类
@Component
public class RedisReceiver {

    public void receiveMessage(String message) throws IOException {
        System.out.println("message:" + message);
        WebSocketServer.sendtoAll(message);
    }
}
