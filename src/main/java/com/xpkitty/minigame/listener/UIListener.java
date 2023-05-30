package com.xpkitty.minigame.listener;

import com.sun.org.apache.xpath.internal.axes.AxesWalker;
import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.GameItemManager;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.Utility;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import com.xpkitty.minigame.instance.game.bedwars.Tool;
import com.xpkitty.minigame.instance.team.Team;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.ui.GameSelectorUI;
import com.xpkitty.minigame.ui.bedwars.Shop;
import com.xpkitty.minigame.ui.bedwars.elements.Item;
import com.xpkitty.minigame.ui.bedwars.elements.ShopCategory;
import com.xpkitty.minigame.ui.game_select.GameCategories;
import com.xpkitty.minigame.ui.shop.OpenKitMenu;
import com.xpkitty.minigame.ui.shop.OpenShopCategory;
import com.xpkitty.minigame.ui.shop.ShopCategories;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.CharacterIterator;
import java.util.Locale;

public class UIListener implements Listener {
    Minigame minigame;

    public UIListener(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains("Player Statistics")) {
            e.setCancelled(true);
        } else if(e.getView().getTitle().contains("Kit selection") && e.getInventory() != null && e.getCurrentItem() != null) {
            e.setCancelled(true);

            KitType type = KitType.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

            Arena arena = minigame.getArenaManager().getArena(player);
            if(arena != null) {
                KitType activated = arena.getKitType(player);
                if(activated != null && activated == type) {
                    player.sendMessage(ChatColor.RED + "You already have this kit equipped.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit");
                    arena.setKit(player.getUniqueId(), type);
                }
                player.closeInventory();
            }


        } else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',ChatColor.BLUE + "Team selection"))) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();

            if(item!=null) {
                ItemMeta itemMeta = item.getItemMeta();

                if (itemMeta != null) {
                    for (Team team : Team.values()) {
                        if (itemMeta.getDisplayName().contains(team.getName())) {
                            if (minigame.getArenaManager().getArena(player) != null) {
                                Arena arena = minigame.getArenaManager().getArena(player);
                                if (!arena.isTeamFull(team, player, false)) {
                                    arena.setTeam(player, team);
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


        } else if(e.getView().getTitle().contains("Minigames") && e.getInventory() != null && e.getCurrentItem() != null) {

            e.setCancelled(true);

            ShopCategories type = ShopCategories.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

            new OpenShopCategory(player, type, minigame);
        } else if(e.getView().getTitle().contains("Kit")) {

            e.setCancelled(true);
            String kitName = e.getCurrentItem().getItemMeta().getLocalizedName();

            PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);

            for(KitType kitType : KitType.values()) {
                if(kitType.name().equalsIgnoreCase(kitName)) {

                    //TODO: KIT BUYING

                    if(!playerDataSave.getKitOwnershipStatus(kitType,player)) {
                        if(playerDataSave.getCoins(player, kitType.getGame()) >= kitType.getPrice()) {

                            playerDataSave.playerJsonDataSave.giveKit(player,kitType);
                            player.sendMessage(ChatColor.GREEN + "Kit purchased: " + kitType.getDisplay());
                            playerDataSave.addPoints(player,kitType.getGame(),-kitType.getPrice());
                            new OpenKitMenu(player,minigame,kitType.getGame());


                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have enough coins");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You already own this kit!");
                    }
                }
            }
        } else if(e.getView().getTitle().contains("Item Shop")) {
            e.setCancelled(true);
            String locName;
            if(e.getCurrentItem().getItemMeta()!=null) {
                e.getCurrentItem().getItemMeta().getLocalizedName();
                ItemStack item = e.getCurrentItem();
                locName=item.getItemMeta().getLocalizedName();
                Item itemVal = null;

                if(locName.contains("bwc_")) {
                    String categoryName = locName.substring(4);
                    ShopCategory category = ShopCategory.valueOf(categoryName);
                    Shop.openShop(player,category);

                    return;
                }

                for(Item element : Item.values()) {
                    if(element.name().equalsIgnoreCase(locName)) {
                        itemVal=element;
                    }
                }

                ItemStack price = new ItemStack(itemVal.getCurrency().getMaterial(), itemVal.getPrice());

                boolean canBuy = true;
                final boolean isArmour = itemVal.equals(Item.CHAIN_ARMOR) || itemVal.equals(Item.IRON_ARMOR) || itemVal.equals(Item.DIAMOND_ARMOR);
                if(isArmour && player.getInventory().containsAtLeast(price, itemVal.getPrice())) {
                    if(itemVal.equals(Item.CHAIN_ARMOR)) {
                       canBuy = GameItemManager.upgradeArmour(player,1,minigame);
                    } else if(itemVal.equals(Item.IRON_ARMOR)) {
                        canBuy = GameItemManager.upgradeArmour(player,2,minigame);
                    } else {
                        canBuy = GameItemManager.upgradeArmour(player,3,minigame);
                    }
                }

                if(player.getInventory().containsAtLeast(price, itemVal.getPrice()) && canBuy) {
                    player.getInventory().removeItem(price);

                    if(itemVal.equals(Item.PICKAXE)) {
                        GameItemManager.upgradeTool(player, Tool.PICKAXE);
                    } else if(itemVal.equals(Item.AXE)) {
                        GameItemManager.upgradeTool(player, Tool.AXE);
                    } else if(itemVal.equals(Item.SHEARS)) {
                        GameItemManager.upgradeTool(player, Tool.SHEARS);
                    } else if(itemVal.equals(Item.KNOCKBACK_STICK)) {
                        GameItemManager.upgradeTool(player, Tool.KNOCKBACK_STICK);
                    } else if(itemVal.equals(Item.FIREBALL)) {
                        GameItemManager.getUtility(player, Utility.FIREBALL);
                    } else if(isArmour) {
                        return;

                        //if bought item is invis potion
                    } else if(itemVal.equals(Item.INVIS)) {
                        GameItemManager.getPotion(player, PotionEffectType.INVISIBILITY, 60, 0);
                    } else if(itemVal.equals(Item.WOOL) || itemVal.equals(Item.GLASS) || itemVal.equals(Item.TERRACOTTA)) {
                        // declare variables
                        Arena arena = minigame.getArenaManager().getArena(player);
                        Material material = Material.STONE;

                        // set material to team colour
                        if(itemVal.equals(Item.WOOL)) {
                            material = arena.getPlayerTeam(player).getWool();
                        } else if(itemVal.equals(Item.GLASS)) {
                            material = arena.getPlayerTeam(player).getGlass();
                        } else if(itemVal.equals(Item.TERRACOTTA)) {
                            material = arena.getPlayerTeam(player).getHardClay();
                        }

                        // make item stack
                        ItemStack teamBlockToGiveToPlayer = new ItemStack(material, itemVal.getCount());

                        // if is glass rename to blast proof glass
                        if(itemVal.equals(Item.GLASS)) {
                            ItemMeta glassMeta = teamBlockToGiveToPlayer.getItemMeta();
                            assert glassMeta != null;
                            glassMeta.setDisplayName(ChatColor.WHITE+"Blast Proof Glass");
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
        } else if(e.getView().getTitle().contains("Game Selector")) {
            e.setCancelled(true);
            ItemStack clickedItem = e.getCurrentItem();
            if(clickedItem!=null) {
                if(clickedItem.getItemMeta()!=null){
                    ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                    String locName = clickedItemMeta.getLocalizedName();

                    if(locName.contains("category_")) {
                        String name = locName.substring(9);
                        GameSelectorUI.openGameSelectorUICategory(player,minigame, GameCategories.valueOf(name.toUpperCase(Locale.ROOT)));
                    } else if(locName.contains("game_")) {
                        String name = locName.substring(5);
                        GameSelectorUI.openGameSelectorUIGame(player,minigame,GameType.valueOf(name.toUpperCase(Locale.ROOT)));
                    } else if(locName.contains("arena_")) {
                        String name = locName.substring(6);
                        int arena = Integer.parseInt(name);

                        minigame.getArenaManager().addPlayerToArena(player,arena);
                    }
                }
            }
        }

        for(ShopCategories value : ShopCategories.values()) {
            if(e.getView().getTitle().contains(value.getDisplay())) {
                e.setCancelled(true);

                if(e.getCurrentItem().getItemMeta()!=null) {
                    switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {

                        default:
                            return;
                        case "kits":
                            new OpenKitMenu((Player) e.getWhoClicked(), minigame, GameType.valueOf(value.name().toUpperCase(Locale.ROOT)));
                            break;
                    }
                }
            }
        }
    }
}
