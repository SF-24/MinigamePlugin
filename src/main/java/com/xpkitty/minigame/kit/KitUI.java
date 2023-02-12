package com.xpkitty.minigame.kit;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.PlayerDataSave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Locale;

public class KitUI {
    Minigame minigame;
    public KitUI(Player player, Minigame minigame, String game) {
        this.minigame = minigame;

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit selection");

        for(KitType type: KitType.values()) {
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            isMeta.setDisplayName(type.getDisplay());
            isMeta.setLore(Arrays.asList(ChatColor.WHITE + type.getDescription()));
            isMeta.setLocalizedName(type.name());
            isMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            isMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            isMeta.addItemFlags(ItemFlag.HIDE_DYE);
            is.setItemMeta(isMeta);

            PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);
            boolean ownsKit = playerDataSave.getKitOwnershipStatus(type.name().toLowerCase(Locale.ROOT), player);
            if(ownsKit && game.equalsIgnoreCase(type.getGame())) {
                gui.addItem(is);
            }
        }

        player.openInventory(gui);
    }
}
