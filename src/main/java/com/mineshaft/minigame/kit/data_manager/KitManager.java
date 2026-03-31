package com.mineshaft.minigame.kit.data_manager;

import com.mineshaft.minigame.Minigame;

public class KitManager {

    public static DynamicKit getDynamicKit(String kitId) {
        return Minigame.getInstance().getKitCache().getKit(kitId);
    }

}
