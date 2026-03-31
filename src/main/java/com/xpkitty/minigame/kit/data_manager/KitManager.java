package com.xpkitty.minigame.kit.data_manager;

import com.xpkitty.minigame.Minigame;

public class KitManager {

    public static DynamicKit getDynamicKit(String kitId) {
        return Minigame.getInstance().getKitCache().getKit(kitId);
    }

}
