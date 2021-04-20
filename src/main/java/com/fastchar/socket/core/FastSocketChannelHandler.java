package com.fastchar.socket.core;

import com.fastchar.core.FastChar;
import com.fastchar.socket.interfaces.IFastTcpSocketByText;
import com.fastchar.socket.interfaces.IFastWebSocketByText;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * @author 沈建（Janesen）
 * @date 2021/4/19 16:20
 */
public class FastSocketChannelHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        List<IFastTcpSocketByText> iFastTcpSocketByTexts = FastChar.getOverrides().singleInstances(false, IFastTcpSocketByText.class);
        for (IFastTcpSocketByText iFastTcpSocketByText : iFastTcpSocketByTexts) {
            if (iFastTcpSocketByText == null) {
                continue;
            }
            iFastTcpSocketByText.onOpen(ctx);
        }
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        } else if (msg instanceof String) {
            List<IFastTcpSocketByText> iFastTcpSocketByTexts = FastChar.getOverrides().singleInstances(false, IFastTcpSocketByText.class);
            for (IFastTcpSocketByText iFastTcpSocketByText : iFastTcpSocketByTexts) {
                if (iFastTcpSocketByText == null) {
                    continue;
                }
                iFastTcpSocketByText.onReceive(ctx, msg.toString());
            }
        }
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (!req.decoderResult().isSuccess() || (!"websocket".contentEquals(req.headers().get("Upgrade")))) {
            return;
        }
        String socketUrl = "ws://" + req.headers().get("host") + req.uri();
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(socketUrl, null, false);
        WebSocketServerHandshaker handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
            FastSocketChannelHelper.addChannel(ctx.channel());
            List<IFastWebSocketByText> iFastWebSocketByTexts = FastChar.getOverrides().singleInstances(false, IFastWebSocketByText.class);
            for (IFastWebSocketByText iFastWebSocketByText : iFastWebSocketByTexts) {
                if (iFastWebSocketByText == null) {
                    continue;
                }
                iFastWebSocketByText.onOpen(ctx, req.uri());
            }
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        List<IFastWebSocketByText> iFastWebSocketByTexts = FastChar.getOverrides().singleInstances(false, IFastWebSocketByText.class);
        if (frame instanceof CloseWebSocketFrame) {
            FastSocketChannelHelper.removeChannel(ctx.channel());
            for (IFastWebSocketByText iFastWebSocketByText : iFastWebSocketByTexts) {
                if (iFastWebSocketByText == null) {
                    continue;
                }
                iFastWebSocketByText.onClose(ctx, (CloseWebSocketFrame) frame);
            }
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
            for (IFastWebSocketByText iFastWebSocketByText : iFastWebSocketByTexts) {
                if (iFastWebSocketByText == null) {
                    continue;
                }
                iFastWebSocketByText.onReceive(ctx, (TextWebSocketFrame) frame);
            }
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        List<IFastTcpSocketByText> iFastTcpSocketByTexts = FastChar.getOverrides().singleInstances(false, IFastTcpSocketByText.class);
        for (IFastTcpSocketByText iFastTcpSocketByText : iFastTcpSocketByTexts) {
            if (iFastTcpSocketByText == null) {
                continue;
            }
            iFastTcpSocketByText.onClose(ctx);
        }
    }

}
