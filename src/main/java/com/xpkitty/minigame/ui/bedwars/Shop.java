// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.ui.bedwars;

import com.xpkitty.minigame.ui.bedwars.elements.Item;
import com.xpkitty.minigame.ui.bedwars.elements.ShopCategory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Shop {

    public Shop(Player player) {
        shop(player);
    }

    public static void shop(Player player) {
        openShop(player,ShopCategory.QUICK_BUY);
    }

    public static void openShop(Player player, ShopCategory currentCategory) {
        Inventory ui = Bukkit.createInventory(null,54, ChatColor.BLACK+"Item Shop");

        // add line of gray glass panes below category area
        for(int i=0; i<9; i++) {
            ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta paneMeta = grayPane.getItemMeta();
            assert paneMeta != null;
            paneMeta.setDisplayName(ChatColor.WHITE + "");
            grayPane.setItemMeta(paneMeta);
            ui.setItem(i+9,grayPane);
        }

        int slotId = 0;
        for(ShopCategory category : ShopCategory.values()) {
            ItemStack stack = new ItemStack(category.getIcon());
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.setDisplayName(category.getName());
            meta.setLocalizedName("bwc_"+category.name());
            stack.setItemMeta(meta);
            ui.addItem(stack);

            // if is open category add green glass pane below
            if(category.equals(currentCategory)) {
                ItemStack greenPane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta paneMeta = greenPane.getItemMeta();
                assert paneMeta != null;
                paneMeta.setDisplayName(ChatColor.WHITE + "");
                greenPane.setItemMeta(paneMeta);
                ui.setItem(slotId+9,greenPane);
            }

            slotId++;
        }

        if(currentCategory.equals(ShopCategory.QUICK_BUY)) {
            for(Item item : Item.values()) {
                if(item.getMainSlot()>-1) {
                    ItemStack stack = new ItemStack(item.getMaterial(),item.getCount());
                    ItemMeta meta = stack.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(ChatColor.WHITE + item.getName());

                    String v = item.getCurrency().getSingular();
                    if(item.getPrice()>1) {
                        v=item.getCurrency().getPlural();
                    }

                    String lore = item.getCurrency().getColorCode()+item.getPrice()+" "+v;
                    meta.setLore(Collections.singletonList(lore));

                    meta.setLocalizedName(item.name());

                    stack.setItemMeta(meta);
                    ui.setItem(item.getMainSlot(),stack);
                }
            }
        } else {
            for(Item item : Item.values()) {
                if(item.getCategory().equals(currentCategory)) {
                    ItemStack stack = new ItemStack(item.getMaterial(),item.getCount());
                    ItemMeta meta = stack.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(ChatColor.WHITE + item.getName());

                    String v = item.getCurrency().getSingular();
                    if(item.getPrice()>1) {
                        v=item.getCurrency().getPlural();
                    }

                    String lore = item.getCurrency().getColorCode()+item.getPrice()+" "+v;
                    meta.setLore(Collections.singletonList(lore));

                    meta.setLocalizedName(item.name());

                    stack.setItemMeta(meta);
                    ui.setItem(item.getSlot(),stack);
                }
            }
        }

        player.openInventory(ui);
    }


}
