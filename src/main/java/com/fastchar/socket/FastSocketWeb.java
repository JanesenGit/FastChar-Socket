package com.fastchar.socket;

import com.fastchar.core.FastEngine;
import com.fastchar.interfaces.IFastWebRun;
import com.fastchar.socket.core.FastSocketHelper;

/**
 * @author 沈建（Janesen）
 * @date 2021/4/19 14:01
 */
public class FastSocketWeb implements IFastWebRun {
    @Override
    public void onRun(FastEngine engine) throws Exception {
        if (engine.getConfig(FastSocketConfig.class).isSocketAutoStart()) {
            FastSocketHelper.getInstance().start();
        }
    }

    @Override
    public void onInit(FastEngine engine) throws Exception {

    }

    @Override
    public void onDestroy(FastEngine engine) throws Exception {
        if (engine.getConfig(FastSocketConfig.class).isSocketAutoStart()) {
            FastSocketHelper.getInstance().stop();
        }
    }
}
