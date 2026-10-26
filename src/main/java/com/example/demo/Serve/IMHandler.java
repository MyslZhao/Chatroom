package com.example.demo.Serve;

import com.example.demo.utils.JWTUntils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Sharable
public class IMHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    public static final Map<Long, Channel> USER_CHANNEL = new ConcurrentHashMap<>();
    private static final AttributeKey<Long> UID = AttributeKey.valueOf("uid");

    /** 静态注入 JwtUtil，供 parseUserId 使用 */
    private static JWTUntils jwtUtil;

    @Autowired
    public void setJwtUtil(JWTUntils util) {
        IMHandler.jwtUtil = util;
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            String uri = ((WebSocketServerProtocolHandler.HandshakeComplete) evt).requestUri();
            Long userId = parseUserId(uri);
            if (userId == null) {
                ctx.close();
                return;
            }
            ctx.channel().attr(UID).set(userId);
            USER_CHANNEL.put(userId, ctx.channel());
            System.out.println("用户 " + userId + " 已连接 WebSocket");
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            // 前端心跳：{"type":"ping"} → 回复 {"type":"pong"}
            if (text.contains("\"type\":\"ping\"")) {
                ctx.writeAndFlush(new TextWebSocketFrame("{\"type\":\"pong\"}"));
            }
            // 其他业务消息暂时不处理（发消息走 HTTP）
        }
    }

    /** 连接断开：清理 Channel 映射 */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Long userId = ctx.channel().attr(UID).get();
        if (userId != null) {
            USER_CHANNEL.remove(userId);
            System.out.println("用户 " + userId + " 已断开");
        }
    }





    private Long parseUserId(String uri) {
        if (uri == null || !uri.contains("token=")) {
            return null;
        }
        String token = uri.substring(uri.indexOf("token=") + 6);
        if (token.contains("&")) {
            token = token.substring(0, token.indexOf("&"));
        }
        try {
            return jwtUtil.getUserId(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static void push(Long recvId, String json) {
        Channel ch = USER_CHANNEL.get(recvId);
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(new TextWebSocketFrame(json));
        }
    }

}