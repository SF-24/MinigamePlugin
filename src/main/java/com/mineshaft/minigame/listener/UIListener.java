// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.mineshaft.minigame.listener;

import com.mineshaft.mineshaftapi.util.formatter.TextFormatter;
import com.mineshaft.mineshaftapi.util.ui.UIUtil;
import com.mineshaft.minigame.Minigame;
import com.mineshaft.minigame.instance.Arena;
import com.mineshaft.minigame.instance.GameItemManager;
import com.mineshaft.minigame.instance.GameType;
import com.mineshaft.minigame.instance.Utility;
import com.mineshaft.minigame.instance.data.PlayerDataSave;
import com.mineshaft.minigame.instance.game.bedwars.Tool;
import com.mineshaft.minigame.instance.team.Team;
import com.mineshaft.minigame.kit.KitType;
import com.mineshaft.minigame.ui.GameSelectorUI;
import com.mineshaft.minigame.ui.bedwars.Shop;
import com.mineshaft.minigame.ui.bedwars.elements.Item;
import com.mineshaft.minigame.ui.bedwars.elements.ShopCategory;
import com.mineshaft.minigame.ui.game_select.GameCategories;
import com.mineshaft.minigame.ui.shop.OpenKitMenu;
import com.mineshaft.minigame.ui.shop.OpenShopCategory;
import com.mineshaft.minigame.ui.shop.ShopCategories;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;

public class UIListener implements Listener {
    Minigame minigame;

    public UIListener(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().contains("Player Statistics")) {
            e.setCancelled(true);
        } else if (e.getView().getTitle().contains("Kit selection") && e.getCurrentItem() != null) {
            e.setCancelled(true);

            String type;
            try {
                type = UIUtil.getOnclick(e.getCurrentItem());
            } catch (Exception ignored) {
                return;
            }

            Arena arena = minigame.getArenaManager().getArena(player);
            if (arena != null) {
                String activated = arena.getKitType(player);
                if (activated != null && activated.equals(type)) {
                    player.sendMessage(ChatColor.RED + "You already have this kit equipped.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "You have equipped the " + TextFormatter.capitaliseStringFully(TextFormatter.addSpacesToString(type)) + ChatColor.GREEN + " kit");
                    // Check if it's dynamic
                    // Shovel-spleef still uses the legacy kit types
                    if (arena.getGameType().equals(GameType.SHOVELSPLEEF)) {
                        arena.setKit(player.getUniqueId(), KitType.valueOf(type));
                    } else {
                        arena.setDynamicKit(player.getUniqueId(), type);
                    }
                    player.closeInventory();
                }
            }
        } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', ChatColor.BLUE + "Team selection"))) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();

            if (item != null) {
                ItemMeta itemMeta = item.getItemMeta();

                if (itemMeta != null) {
                    for (Team team : Team.values()) {
                        if (itemMeta.getDisplayName().contains(team.getName())) {
                            if (minigame.getArenaManager().getArena(player) != null) {
                                Arena gameArena = minigame.getArenaManager().getArena(player);
                                if (!gameArena.isTeamFull(team, player, false)) {
                                    gameArena.setTeam(player, team);
                                    player.sendMessage("You are now in " + team.getColorCode() + team.getName() + " team");
                                    player.closeInventory();
                                } else {
                                    player.sendMessage(ChatColor.RED + "Team " + team.getName() + " is full");
                                }
                            }
                        }
                    }
                }
            }


        } else if (e.getView().getTitle().contains("Minigames") && e.getCurrentItem() != null) {

            e.setCancelled(true);
            new OpenShopCategory(player, ShopCategories.valueOf(UIUtil.getOnclick(e.getCurrentItem())), minigame);
        } else if (e.getView().getTitle().contains("Kit")) {

            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            String kitName = UIUtil.getOnclick(e.getCurrentItem());

            PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);

            for (KitType kitType : KitType.values()) {
                if (kitType.name().equalsIgnoreCase(kitName)) {

                    //TODO: KIT BUYING

                    if (!playerDataSave.getKitOwnershipStatus(kitType.name(), player)) {
                        if (playerDataSave.getCoins(player, kitType.getGame()) >= kitType.getPrice()) {

                            playerDataSave.playerJsonDataSave.giveKit(player, kitType.name());
                            player.sendMessage(ChatColor.GREEN + "Kit purchased: " + kitType.getDisplay());
                            playerDataSave.addPoints(player, kitType.getGame(), -kitType.getPrice());
                            new OpenKitMenu(player, minigame, kitType.getGame());


                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have enough coins");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You already own this kit!");
                    }
                }
            }
            // Dynamic kits
            for (String kitType : Minigame.getInstance().getKitCache().getKits()) {
                if (kitType.equalsIgnoreCase(kitName)) {

                    //TODO: KIT BUYING

                    if (!playerDataSave.getKitOwnershipStatus(kitType, player)) {
                        if (playerDataSave.getCoins(player, GameType.valueOf(Minigame.getInstance().getKitCache().getKit(kitType).games.get(0).toUpperCase())) >= Minigame.getInstance().getKitCache().getKit(kitType).getCost()) {

                            playerDataSave.playerJsonDataSave.giveKit(player, kitType);
                            player.sendMessage(ChatColor.GREEN + "Kit purchased: " + TextFormatter.convertStringToName(kitType));
                            playerDataSave.addPoints(player, GameType.valueOf(Minigame.getInstance().getKitCache().getKit(kitType).games.get(0).toUpperCase()), -Minigame.getInstance().getKitCache().getKit(kitType).getCost());
                            new OpenKitMenu(player, minigame, GameType.valueOf(Minigame.getInstance().getKitCache().getKit(kitType).games.get(0).toUpperCase()));


                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have enough coins");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You already own this kit!");
                    }
                }
            }
        } else if (e.getView().getTitle().contains("Item Shop")) {
            e.setCancelled(true);
            String locName;
            if (e.getCurrentItem().getItemMeta() != null) {
                ItemStack item = e.getCurrentItem();
                locName = UIUtil.getOnclick(item);
                Item itemVal = null;

                if (locName.contains("bwc_")) {
                    String categoryName = locName.substring(4);
                    ShopCategory category = ShopCategory.valueOf(categoryName);
                    Shop.openShop(player, category);

                    return;
                }

                for (Item element : Item.values()) {
                    if (element.name().equalsIgnoreCase(locName)) {
                        itemVal = element;
                    }
                }

                ItemStack price = new ItemStack(itemVal.getCurrency().getMaterial(), itemVal.getPrice());

                boolean canBuy = true;
                final boolean isArmour = itemVal.equals(Item.CHAIN_ARMOR) || itemVal.equals(Item.IRON_ARMOR) || itemVal.equals(Item.DIAMOND_ARMOR);
                if (isArmour && player.getInventory().containsAtLeast(price, itemVal.getPrice())) {
                    if (itemVal.equals(Item.CHAIN_ARMOR)) {
                        canBuy = GameItemManager.upgradeArmour(player, 1, minigame);
                    } else if (itemVal.equals(Item.IRON_ARMOR)) {
                        canBuy = GameItemManager.upgradeArmour(player, 2, minigame);
                    } else {
                        canBuy = GameItemManager.upgradeArmour(player, 3, minigame);
                    }
                }

                if (player.getInventory().containsAtLeast(price, itemVal.getPrice()) && canBuy) {
                    player.getInventory().removeItem(price);

                    if (itemVal.equals(Item.PICKAXE)) {
                        GameItemManager.upgradeTool(player, Tool.PICKAXE);
                    } else if (itemVal.equals(Item.AXE)) {
                        GameItemManager.upgradeTool(player, Tool.AXE);
                    } else if (itemVal.equals(Item.SHEARS)) {
                        GameItemManager.upgradeTool(player, Tool.SHEARS);
                    } else if (itemVal.equals(Item.KNOCKBACK_STICK)) {
                        GameItemManager.upgradeTool(player, Tool.KNOCKBACK_STICK);
                    } else if (itemVal.equals(Item.FIREBALL)) {
                        GameItemManager.getUtility(player, Utility.FIREBALL);
                    } else if (isArmour) {
                        return;

                        //if bought item is invis potion
                    } else if (itemVal.equals(Item.INVIS)) {
                        GameItemManager.getPotion(player, PotionEffectType.INVISIBILITY, 60, 0);
                    } else if (itemVal.equals(Item.WOOL) || itemVal.equals(Item.GLASS) || itemVal.equals(Item.TERRACOTTA)) {
                        // declare variables
                        Material material = Material.STONE;

                        // set material to team colour
                        if (itemVal.equals(Item.WOOL)) {
                            material = minigame.getArenaManager().getArena(player).getPlayerTeam(player).getWool();
                        } else if (itemVal.equals(Item.GLASS)) {
                            material = minigame.getArenaManager().getArena(player).getPlayerTeam(player).getGlass();
                        } else if (itemVal.equals(Item.TERRACOTTA)) {
                            material = minigame.getArenaManager().getArena(player).getPlayerTeam(player).getHardClay();
                        }

                        // make item stack
                        ItemStack teamBlockToGiveToPlayer = new ItemStack(material, itemVal.getCount());

                        // if is glass rename to blast proof glass
                        if (itemVal.equals(Item.GLASS)) {
                            ItemMeta glassMeta = teamBlockToGiveToPlayer.getItemMeta();
                            assert glassMeta != null;
                            glassMeta.setDisplayName(ChatColor.WHITE + "Blast Proof Glass");
                            teamBlockToGiveToPlayer.setItemMeta(glassMeta);
                        }

                        // give player item
                        player.getInventory().addItem(teamBlockToGiveToPlayer);
                    } else {
                        ItemStack giveItem = GameItemManager.getUnbreakableItem(itemVal.getMaterial());
                        giveItem.setAmount(itemVal.getCount());

                        player.getInventory().addItem(giveItem);
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "Not enough " + itemVal.getCurrency().name().toLowerCase(Locale.ROOT));
                }
            }
        } else if (e.getView().getTitle().contains("Game Selector")) {
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem != null) {
                if (clickedItem.getItemMeta() != null) {
                    String locName = UIUtil.getOnclick(clickedItem);

                    if (locName.contains("category_")) {
                        String name = locName.substring(9);
                        GameSelectorUI.openGameSelectorUICategory(player, minigame, GameCategories.valueOf(name.toUpperCase(Locale.ROOT)));
                    } else if (locName.contains("game_")) {
                        String name = locName.substring(5);
                        GameSelectorUI.openGameSelectorUIGame(player, minigame, GameType.valueOf(name.toUpperCase(Locale.ROOT)));
                    } else if (locName.contains("arena_")) {
                        String name = locName.substring(6);
                        minigame.getArenaManager().addPlayerToArena(player, Integer.parseInt(name));
                    }
                }
            }
        }

        for (ShopCategories value : ShopCategories.values()) {
            if (e.getView().getTitle().contains(value.getDisplay())) {
                e.setCancelled(true);

                if (e.getCurrentItem().getItemMeta() != null) {
                    if ("kits".equals(UIUtil.getOnclick(e.getCurrentItem()))) {
                        new OpenKitMenu((Player) e.getWhoClicked(), minigame, GameType.valueOf(value.name().toUpperCase(Locale.ROOT)));
                    } else {
                        return;
                    }
                }
            }
        }
    }

}
