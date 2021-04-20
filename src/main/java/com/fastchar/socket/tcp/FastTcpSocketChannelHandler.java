package com.fastchar.socket.tcp;

import com.fastchar.core.FastChar;
import com.fastchar.socket.core.FastSocketChannelHelper;
import com.fastchar.socket.interfaces.IFastTcpSocketByText;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;
import java.util.List;

/**
 * @author 沈建（Janesen）
 * @date 2021/3/31 09:31
 */
public class FastTcpSocketChannelHandler extends SimpleChannelInboundHandler<String> {

    private void notifyListener(ChannelHandlerContext ctx) {
        List<IFastTcpSocketByText> iFastTcpSocketByTexts = FastChar.getOverrides().singleInstances(false, IFastTcpSocketByText.class);
        for (IFastTcpSocketByText iFastTcpSocketByText : iFastTcpSocketByTexts) {
            if (iFastTcpSocketByText == null) {
                continue;
            }
            iFastTcpSocketByText.onOpen(ctx);
        }
        FastSocketChannelHelper.addChannel(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        notifyListener(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        notifyListener(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String o) throws Exception {
        List<IFastTcpSocketByText> iFastTcpSocketByTexts = FastChar.getOverrides().singleInstances(false, IFastTcpSocketByText.class);
        for (IFastTcpSocketByText iFastTcpSocketByText : iFastTcpSocketByTexts) {
            if (iFastTcpSocketByText == null) {
                continue;
            }
            if (o != null) {
                iFastTcpSocketByText.onReceive(ctx, o.toString());
            }else{
                iFastTcpSocketByText.onReceive(ctx, null);
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
        FastSocketChannelHelper.removeChannel(ctx.channel());
    }
}