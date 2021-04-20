package com.fastchar.socket;

import com.fastchar.interfaces.IFastConfig;

/**
 * @author 沈建（Janesen）
 * @date 2021/4/19 13:41
 */
public class FastSocketConfig implements IFastConfig {

    private boolean debug;
    private int socketPort=8888;//socket端口
    private boolean socketAutoStart;//是否自动启动WebSocket

    public boolean isDebug() {
        return debug;
    }

    public FastSocketConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public FastSocketConfig setSocketPort(int socketPort) {
        this.socketPort = socketPort;
        return this;
    }

    public boolean isSocketAutoStart() {
        return socketAutoStart;
    }

    public FastSocketConfig setSocketAutoStart(boolean socketAutoStart) {
        this.socketAutoStart = socketAutoStart;
        return this;
    }
}
