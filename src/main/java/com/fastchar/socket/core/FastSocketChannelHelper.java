package com.fastchar.socket.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 沈建（Janesen）
 * @date 2021/3/31 14:12
 */
public class FastSocketChannelHelper {

    private static final ChannelGroup GLOBAL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final ConcurrentMap<String, ChannelId> CHANNEL_MAP = new ConcurrentHashMap<>();

    public static void addChannel(Channel channel) {
        GLOBAL_GROUP.add(channel);
        CHANNEL_MAP.put(channel.id().asShortText(), channel.id());
    }

    public static void removeChannel(Channel channel) {
        GLOBAL_GROUP.remove(channel);
        CHANNEL_MAP.remove(channel.id().asShortText());
    }

    public static Channel findChannel(String id) {
        ChannelId channelId = CHANNEL_MAP.get(id);
        if (channelId == null) {
            return null;
        }
        return GLOBAL_GROUP.find(channelId);
    }
}
