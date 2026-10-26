package com.example.demo.Serve;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyServe implements CommandLineRunner {

    @Autowired
    private WsServerInitializer wsServerInitializer;

    @Override
    public void run(String... args) throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(wsServerInitializer);

            ChannelFuture future = b.bind(8878).sync();
            System.out.println("Netty WebSocket 启动，端口 8878");
            // 不调用 closeFuture().sync()，让 run 方法正常返回
        } catch (Exception e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            throw e;
        }
    }
}