package com.fastchar.socket.interfaces;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author 沈建（Janesen）
 * @date 2021/4/19 13:44
 */
public interface IFastTcpSocketByText {

    void onOpen(ChannelHandlerContext ctx);

    void onReceive(ChannelHandlerContext ctx, String msg);

    void onClose(ChannelHandlerContext ctx);

}
