package com.xpkitty.minigame.listener;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import com.xpkitty.minigame.instance.team.Team;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.shop.OpenKitMenu;
import com.xpkitty.minigame.shop.OpenShopCategory;
import com.xpkitty.minigame.shop.ShopCategories;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UIListener implements Listener {
    Minigame minigame;

    public UIListener(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains("Kit selection") && e.getInventory() != null && e.getCurrentItem() != null) {
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
            ItemMeta itemMeta = item.getItemMeta();

            if(itemMeta!=null) {
                for(Team team : Team.values()) {
                    if(itemMeta.getDisplayName().contains(team.getName())) {
                        if(minigame.getArenaManager().getArena(player)!=null) {
                            Arena arena = minigame.getArenaManager().getArena(player);
                            if(!arena.isTeamFull(team,player,false)) {
                                arena.setTeam(player,team);
                                player.sendMessage("You are now in " + team.getColorCode() + team.getName() + " team");
                                player.closeInventory();
                            } else {
                                player.sendMessage(ChatColor.RED+ "Team " + team.getName() + " is full");
                            }
                        }
                    }
                }
            }



        } else if(e.getView().getTitle().contains("Minigame shop") && e.getInventory() != null && e.getCurrentItem() != null) {

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

                    if(!playerDataSave.getKitOwnershipStatus(kitName,player)) {
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



        }

        for(ShopCategories value : ShopCategories.values()) {
            if(e.getView().getTitle().contains(value.getDisplay())) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {

                    default:
                        return;
                    case "kits":
                        new OpenKitMenu((Player) e.getWhoClicked(), minigame, value.name());
                        break;
                }
            }
        }
    }
}
