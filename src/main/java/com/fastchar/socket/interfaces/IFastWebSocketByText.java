package com.fastchar.socket.interfaces;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author 沈建（Janesen）
 * @date 2021/4/19 13:43
 */
public interface IFastWebSocketByText {

    void onOpen(ChannelHandlerContext ctx, String uri);

    void onReceive(ChannelHandlerContext ctx, TextWebSocketFrame msgFrame);

    void onClose(ChannelHandlerContext ctx, CloseWebSocketFrame closeFrame);

}
