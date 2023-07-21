// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.kit;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class KitUI {
    Minigame minigame;
    public KitUI(Player player, Minigame minigame, GameType game) {
        this.minigame = minigame;

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit selection");

        for(KitType type: KitType.values()) {
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            assert isMeta != null;
            isMeta.setDisplayName(type.getDisplay());
            isMeta.setLore(Collections.singletonList(ChatColor.WHITE + type.getDescription()));
            isMeta.setLocalizedName(type.name());
            isMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            isMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            isMeta.addItemFlags(ItemFlag.HIDE_DYE);
            is.setItemMeta(isMeta);

            PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);
            boolean ownsKit = playerDataSave.getKitOwnershipStatus(type, player);
            if(ownsKit && game.equals(type.getGame())) {
                gui.addItem(is);
            }
        }

        player.openInventory(gui);
    }
}
