package com.zm.netty.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class NettyServer {
	
    private static int port=8899;
    
    public void start() throws Exception {
    	System.out.println("端口："+port);
        EventLoopGroup bossGroup = new NioEventLoopGroup(20);
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(group, bossGroup)
               .channel(NioServerSocketChannel.class)
               .localAddress(this.port)
               .childHandler(new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel ch) throws Exception {
                     System.out.println("收到新连接:"+ch.localAddress());
                     ch.pipeline().addLast(new HttpServerCodec());
                     ch.pipeline().addLast(new ChunkedWriteHandler());
                     ch.pipeline().addLast(new HttpObjectAggregator(8192));
                     ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", "WebSocket", true, 65536 * 10));
                     ch.pipeline().addLast(new MyWebSocketHandler());
                   }
                });
            ChannelFuture cf = sb.bind(port).sync(); // 服务器异步创建绑
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }
}
