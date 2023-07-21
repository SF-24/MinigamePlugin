// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.kit;

import com.xpkitty.minigame.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class Kit implements Listener {

    protected KitType type;
    protected UUID uuid;

    public Kit(Minigame minigame, KitType type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;

        Bukkit.getPluginManager().registerEvents(this, minigame);
    }

    public UUID getUuid() { return uuid;}
    public KitType getType() { return type; }

    public abstract void onStart(Player player);

    public void remove() {
        HandlerList.unregisterAll(this);
    }

}
