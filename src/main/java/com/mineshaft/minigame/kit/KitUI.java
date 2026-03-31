// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.mineshaft.minigame.kit;

import com.mineshaft.mineshaftapi.util.ui.UIUtil;
import com.mineshaft.minigame.Minigame;
import com.mineshaft.minigame.instance.GameType;
import com.mineshaft.minigame.instance.data.PlayerDataSave;
import com.mineshaft.minigame.kit.data_manager.DynamicKit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class KitUI {
    Minigame minigame;
    public KitUI(Player player, Minigame minigame, GameType game) {
        this.minigame = minigame;

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit selection");

        if(game.hasLegacyKits()) {
            for (KitType type : KitType.values()) {
                ItemStack is = new ItemStack(type.getMaterial());
                ItemMeta isMeta = is.getItemMeta();
                assert isMeta != null;
                isMeta.setDisplayName(type.getDisplay());
                isMeta.setLore(Collections.singletonList(ChatColor.WHITE + type.getDescription()));
                isMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                isMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
                isMeta.addItemFlags(ItemFlag.HIDE_DYE);
                is.setItemMeta(isMeta);

                UIUtil.setOnclick(is, type.name());

                PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);
                boolean ownsKit = playerDataSave.getKitOwnershipStatus(type.name(), player);
                if (ownsKit && game.equals(type.getGame())) {
                    gui.addItem(is);
                }
            }
        } else {
            // Dynamic kit ui
            for(String type : Minigame.getInstance().getKitCache().getKits()) {
                DynamicKit kit =  Minigame.getInstance().getKitCache().getKit(type);
                PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);

                boolean ownsKit = playerDataSave.getKitOwnershipStatus(type, player);
                if (ownsKit && kit.isValidGame(game)) {
                    gui.addItem(kit.getIcon(true));
                }
            }
        }

        player.openInventory(gui);
    }
}
