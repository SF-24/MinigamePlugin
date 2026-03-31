package com.xpkitty.minigame.kit.data_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class YamlKitCache {

    private HashMap<String, DynamicKit> minigameKits;

    public DynamicKit getKit(String id) {
        return  minigameKits.get(id);
    }

    public Set<String> getKits() {
        return minigameKits.keySet();
    }

    public void cacheKit(String id, DynamicKit dynamicKit) {
        minigameKits.put(id, dynamicKit);
    }

    public void clearCache() {
        minigameKits.clear();
    }
}
