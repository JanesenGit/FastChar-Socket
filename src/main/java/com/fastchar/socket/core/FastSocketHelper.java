package com.fastchar.socket.core;

import com.fastchar.core.FastChar;
import com.fastchar.socket.FastSocketConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author 沈建（Janesen）
 * @date 2021/4/19 14:01
 */
public class FastSocketHelper implements Runnable {

    public static FastSocketHelper getInstance() {
        return FastChar.getOverrides().singleInstance(FastSocketHelper.class);
    }

    private FastSocketHelper() {

    }

    private boolean start;
    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup childGroup = new NioEventLoopGroup();

    @Override
    public void run() {
        if (start) {
            return;
        }
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 300)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new FastSocketChannelInitializer());

            InetSocketAddress localAddress = new InetSocketAddress(FastChar.getConfig(FastSocketConfig.class).getSocketPort());
            ChannelFuture channelFuture = serverBootstrap.bind(localAddress).sync();
            start = true;

            if (FastChar.getConfig(FastSocketConfig.class).isDebug()) {
                FastChar.getLog().info("Socket启动成功！端口：" + localAddress.getPort());
            }

            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            start = false;
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
        start = false;
        if (FastChar.getConfig(FastSocketConfig.class).isDebug()) {
            FastChar.getLog().info("Socket停止成功！端口：" + FastChar.getConfig(FastSocketConfig.class).getSocketPort());
        }
    }

    public boolean isStart() {
        return start;
    }

    public boolean webSocketSend(String channelShortId, String text) {
        Channel channel = FastSocketChannelHelper.findChannel(channelShortId);
        if (channel == null) {
            return false;
        }
        channel.writeAndFlush(new TextWebSocketFrame(text));
        return true;
    }

    public boolean tcpSocketSend(String channelShortId, Object text) {
        Channel channel = FastSocketChannelHelper.findChannel(channelShortId);
        if (channel == null) {
            return false;
        }
        channel.writeAndFlush(text);
        return true;
    }

}
