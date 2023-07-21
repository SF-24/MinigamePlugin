// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.ui;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.ui.game_select.GameCategories;
import com.xpkitty.minigame.ui.game_select.GameSelectorUIArea;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public class GameSelectorUI {

    public static void openMainGameSelectorUI(Player player, Minigame minigame) {
        openGameSelectorUI(player,minigame,GameSelectorUIArea.GAME_SELECT,null,null);
    }

    public static void openGameSelectorUICategory(Player player, Minigame minigame, GameCategories category) {
        openGameSelectorUI(player,minigame,GameSelectorUIArea.CATEGORY,null,category);
    }

    public static void openGameSelectorUIGame(Player player, Minigame minigame, GameType gameType) {
        openGameSelectorUI(player,minigame,GameSelectorUIArea.MINIGAME_ARENAS,gameType,null);
    }

    public static void openGameSelectorUI(Player player, Minigame minigame, GameSelectorUIArea menu, GameType game, GameCategories category) {
        Inventory ui = Bukkit.createInventory(null,27, ChatColor.BLACK + "Game Selector");

        for(int i = 0; i<ui.getSize(); i++) {
            // Create item stack and meta
            ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta glassPaneMeta = glassPane.getItemMeta();

            // name them
            assert glassPaneMeta != null;
            glassPaneMeta.setDisplayName(ChatColor.WHITE + "");

            // apply meta and add to ui
            glassPane.setItemMeta(glassPaneMeta);
            ui.setItem(i,glassPane);
        }

        // if the menu to open is game select
        if(menu.equals(GameSelectorUIArea.GAME_SELECT)) {
            for (GameCategories element : GameCategories.values()) {
                // Create icon item
                ItemStack item = new ItemStack(element.getIcon());
                ItemMeta itemMeta = item.getItemMeta();

                // Set name and description
                assert itemMeta != null;
                itemMeta.setDisplayName(ChatColor.WHITE + element.getName());
                itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + element.getDescription()));

                // Set hide flags
                itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
                itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

                // set localized name
                itemMeta.setLocalizedName("category_" + element.name());

                // apply item meta
                item.setItemMeta(itemMeta);

                // add to ui
                ui.setItem(element.getGameSelectorSlot(), item);
            }
        } else if(menu.equals(GameSelectorUIArea.CATEGORY)) {
            if(category!=null) {
                // open player category ui

                // declare slot
                int i =10;

                // loop through minigames list
                for(GameType element : GameType.values()) {
                    boolean canAddMinigame = true;

                    //getting player count
                    int players = 0;

                    // get players count
                    for(Arena arena : minigame.getArenaManager().getArenas()) {
                        if(arena.getGameType().equals(element)) {
                            players+=arena.getPlayers().size();
                        }
                    }

                    // don't show element if category is null
                    if(element.getCategory()==null) {
                        canAddMinigame=false;
                    }

                    // test if minigame should be added to ui category
                    if(category!=GameCategories.ALL && canAddMinigame) {
                        if(!element.getCategory().equals(category)) {
                            canAddMinigame=false;
                        }
                    }

                    // if it should be added declare item stack and add to ui
                    if(canAddMinigame) {
                        ItemStack item = new ItemStack(element.getIcon());
                        ItemMeta meta = item.getItemMeta();

                        // set name, etc.
                        assert meta != null;
                        meta.setDisplayName(ChatColor.WHITE + element.getDisplayName());
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                        String peoplePlayingString = " people playing";
                        if(players==1) {
                            peoplePlayingString = " person playing";
                        }

                        meta.setLore(Collections.singletonList(ChatColor.GRAY.toString() + players + peoplePlayingString));

                        // set localized name
                        meta.setLocalizedName("game_" + element.name());

                        // apply meta
                        item.setItemMeta(meta);

                        // add to ui
                        ui.setItem(i,item);

                        // increase slot by 1
                        i++;
                    }
                }
            } else {
                player.sendMessage("ERROR! Specified category is null. Please contact a server administrator or report bug at: https://github.com/XpKitty/MinigamePlugin");
            }
        } else if(menu.equals(GameSelectorUIArea.MINIGAME_ARENAS)) {

            // Declare item slot
            int i=0;

            // Loop through arenas
            for(Arena arena : minigame.getArenaManager().getArenas()) {

                // if arena is of game the player is looking at
                if(arena.getGameType().equals(game)) {

                    // declare item-stack and meta
                    ItemStack stack = new ItemStack(arena.getState().getIcon());
                    ItemMeta meta = stack.getItemMeta();

                    // set name
                    assert meta != null;
                    meta.setDisplayName(ChatColor.WHITE + "Arena " + arena.getId());

                    // set localized name
                    meta.setLocalizedName("arena_" + arena.getId());

                    // set lore (description)
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(arena.getState().getDisplayName());
                    lore.add(ChatColor.GRAY + String.valueOf(arena.getPlayers().size()) + "/"+ arena.getMaxPlayers() +" playing");

                    if (arena.getState().equals(GameState.RECRUITING)) {
                        int requiredPlayers = arena.getRequiredPlayers()-arena.getPlayers().size();
                        lore.add(ChatColor.GRAY + String.valueOf(requiredPlayers ) + " more players required to start");
                    }
                    meta.setLore(lore);

                    // apply meta
                    stack.setItemMeta(meta);

                    //add to ui
                    ui.setItem(i, stack);

                    //increase slot for next item
                    i++;

                    if(i>ui.getSize()) {
                        player.sendMessage(ChatColor.RED + "ERROR! not enough room in ui for item. Please report this error to an administrator or post it on github at: https://github.com/XpKitty/MinigamePlugin");
                        System.out.println("");
                        System.out.println("[MinigamePlugin] ERROR! not enough room in ui for item. Please report this error to an administrator or post it on github at: https://github.com/XpKitty/MinigamePlugin");
                        System.out.println("");
                    }
                }
            }
        }

        player.openInventory(ui);
    }

}
