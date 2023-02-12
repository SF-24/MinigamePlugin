package com.xpkitty.minigame.instance.game;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.Game;
import com.xpkitty.minigame.instance.PlayerDataSave;
import com.xpkitty.minigame.listener.ConnectListener;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KnockoutGame extends Game {
    private static FileConfiguration config;

    List<UUID> alivePlayers = new ArrayList<>();
    Minigame minigame;
    ConnectListener connectListener;

    int coinsForWin = 10;
    Player lastdead;
    Player winner;


    public KnockoutGame(Minigame minigame, Arena arena, ConnectListener connectListener) {
        super(minigame, arena, connectListener);

        this.connectListener = connectListener;
        this.minigame = minigame;
    }


    public void giveWin(Player player) {
        World world = arena.getWorld();


        for(UUID uuid : arena.getPlayers()) {
            Bukkit.getPlayer(uuid).setHealth(20);
            Bukkit.getPlayer(uuid).setFoodLevel(20);
            for(PotionEffect effect: Bukkit.getPlayer(uuid).getActivePotionEffects()) { Bukkit.getPlayer(uuid).removePotionEffect(effect.getType()); }
        }
        player.sendTitle(ChatColor.GREEN + "VICTORY","",10,100,10);



        player.sendMessage(ChatColor.GOLD + "+" + coinsForWin + " coins");

        minigame.getApi().giveHousePoints(player,5,true);

        PlayerDataSave dataSave = connectListener.getPlayerData(player);
        if(dataSave != null) {
            dataSave.addPoints(player, "coins" ,"SHOVELSPLEEF", coinsForWin);
            System.out.println("added " + coinsForWin + " SPLEEF coins to " + player.getDisplayName());
        } else {
            System.out.println("data save is null!");
        }


        player.sendMessage(ChatColor.RED + "VICTORY! YOU WIN!");
        arena.sendMessage(ChatColor.GOLD + player.getName() + " HAS WON!" + " Thanks for playing.");
        arena.clearInventory();


        Bukkit.getScheduler().runTaskLater(minigame, () -> {
            arena.sendMessage(ChatColor.GOLD + "Game has ended. You have been moved to lobby");
            arena.sendTitle("","");
            arena.reset(false, 0);
        }, 300);
    }


    @Override
    public void onStart() {

        arena.sendMessage(ChatColor.GREEN + "GAME HAS STARTED! Hit other players to knock the into lava. The last person alive wins. Good luck!");
        arena.clearInventory();

        for(UUID uuid: arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            player.setHealth(20);
            player.setFoodLevel(20);
            alivePlayers.add(uuid);
            for(PotionEffect effect: player.getActivePotionEffects()) { Bukkit.getPlayer(uuid).removePotionEffect(effect.getType()); }
            player.setGameMode(GameMode.ADVENTURE);
            player.closeInventory();
        }

        ItemStack knockback = new ItemStack(Material.STICK);
        ItemMeta knockbackMeta = knockback.getItemMeta();
        knockbackMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        knockbackMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Knockback Stick");
        knockback.setItemMeta(knockbackMeta);

        arena.giveItem(knockback);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {


        if(arena.getPlayers().contains(e.getEntity().getUniqueId())) {
            if(arena.getState() == GameState.LIVE) {
                arena.sendMessage(ChatColor.AQUA + e.getEntity().getDisplayName() + ChatColor.RESET + " has been eliminated!");
                e.getEntity().setGameMode(GameMode.ADVENTURE);



                if(alivePlayers.contains(e.getEntity().getUniqueId())) {
                    Bukkit.getScheduler().runTaskLater(minigame, () -> {
                        e.getEntity().teleport(arena.getRespawn());
                    }, 20);
                }

                alivePlayers.remove(e.getEntity().getUniqueId());
                if(alivePlayers.size() == 1) {
                    arena.clearInventory();
                    lastdead = e.getEntity();
                    winner = Bukkit.getPlayer(alivePlayers.get(0));
                }
            }
        }
        e.getEntity().getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
        e.getEntity().getWorld().setGameRule(GameRule.SHOW_DEATH_MESSAGES,false);



    }

    @EventHandler
    public void onEvent(PlayerMoveEvent e) {
        if(arena.getPlayers().size() < alivePlayers.size()) {
            alivePlayers.removeIf(uuid -> !arena.getPlayers().contains(uuid));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if(alivePlayers.size() == 1) {
            if (lastdead.getName().equals(e.getPlayer().getName())) {
                giveWin(winner);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(!(e.getBlock().getType() == Material.SNOW_BLOCK)) {
            if(arena.getPlayers().contains(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }
}
