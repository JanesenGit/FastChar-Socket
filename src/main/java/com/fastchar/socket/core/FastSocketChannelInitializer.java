package com.fastchar.socket.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author 沈建（Janesen）
 * @date 2021/3/31 09:30
 */
public class FastSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("FastSocketChooseHandler", new FastSocketChooseHandler());
    }
}
