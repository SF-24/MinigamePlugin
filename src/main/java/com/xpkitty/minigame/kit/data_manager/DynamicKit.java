package com.xpkitty.minigame.kit.data_manager;

import com.mineshaft.mineshaftapi.util.formatter.TextFormatter;
import com.mineshaft.mineshaftapi.util.item.ItemImportUtil;
import com.mineshaft.mineshaftapi.util.ui.UIUtil;
import com.xpkitty.minigame.instance.GameType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.plaf.TextUI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DynamicKit {

    private final int cost;
    public HashMap<String, Integer> items;
    public HashMap<EquipmentSlot, String> equipment;
    public List<String> games;
    public String icon;
    String id;

    public DynamicKit(String id, int cost, List<String> games, String icon, HashMap<String, Integer> items, HashMap<EquipmentSlot, String> equipment) {
        this.cost=cost;
        this.items=items;
        this.equipment=equipment;
        for(String game : games) {
            this.games.add(game.toLowerCase());
        }
        this.icon=icon;
        this.id=id;
    }

    public void onStart(Player player) {
        for(EquipmentSlot equipmentSlot : equipment.keySet()) {
            player.getInventory().setItem(equipmentSlot, ItemImportUtil.getItemFromString(equipment.get(equipmentSlot)));
        }
        for (String item : items.keySet()) {
            ItemStack itemStack = ItemImportUtil.getItemFromString(item);
            itemStack.setAmount(items.get(item));
            player.getInventory().addItem(itemStack);
        }
    }

    public int getCost() {
        return cost;
    }

    public boolean isValidGame(GameType gameType) {
        return games.contains(gameType.name().toLowerCase());
    }

    public ItemStack getIcon(boolean clickToSelect) {
        ItemStack iconItem = ItemImportUtil.getItemFromString(icon);
        iconItem.setAmount(1);
        ItemMeta iconMeta = iconItem.getItemMeta();
        iconMeta.setDisplayName(ChatColor.WHITE + TextFormatter.capitaliseString(id));
        if(clickToSelect) {
            iconMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to select."));
        } else {
            iconMeta.setLore(Collections.singletonList(" "));
        }
        iconItem.setItemMeta(iconMeta);
        UIUtil.setOnclick(iconItem,id);
        return iconItem;
    }

}
