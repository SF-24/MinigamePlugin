// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance.game;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.Game;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ConfigManager;
import com.xpkitty.minigame.manager.Region;
import com.xpkitty.minigame.manager.statistics.StatisticType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShovelSpleef extends Game {

    List<UUID> alivePlayers = new ArrayList<>();
    Minigame minigame;
    ConnectListener connectListener;

    int coinsForWin = 10;
    Player lastDead;
    Player winner;


    public ShovelSpleef(Minigame minigame, Arena arena, ConnectListener connectListener) {
        super(minigame, arena, connectListener);

        this.connectListener = connectListener;
        this.minigame = minigame;
    }


     public void giveWin(Player player) {
         resetArena(true);

        for(UUID uuid : arena.getPlayers()) {
            Player uuidPlayer = Bukkit.getPlayer(uuid);
            assert uuidPlayer!=null;
            uuidPlayer.setHealth(20);
            uuidPlayer.setFoodLevel(20);
            for(PotionEffect effect: uuidPlayer.getActivePotionEffects()) { uuidPlayer.removePotionEffect(effect.getType()); }
        }
        player.sendTitle(ChatColor.GREEN + "VICTORY","",10,100,10);



        player.sendMessage(ChatColor.GOLD + "+" + coinsForWin + " coins");
        PlayerDataSave dataSave = connectListener.getPlayerData(player);
        if(dataSave != null) {
            dataSave.addPoints(player, GameType.SHOVELSPLEEF, coinsForWin);
            dataSave.getPlayerJsonDataSave().addStatisticForLatestSeason(GameType.SHOVELSPLEEF,player, StatisticType.WINS);
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
        Block newBlock = arena.getWorld().getBlockAt(0, 63, 0);
        newBlock.setType(Material.SNOW_BLOCK);

        resetArena(false);

        arena.sendMessage(ChatColor.GREEN + "GAME HAS STARTED! Dig under other players to knock the into lava. The last person alive wins. Good luck!");
        arena.clearInventory();

        Bukkit.getScheduler().runTaskLater(minigame, () -> {
            for(UUID uuid : arena.getPlayers()) {
                Player player = Bukkit.getPlayer(uuid);
                assert player != null;
                player.removePotionEffect(PotionEffectType.WEAKNESS);
            }
        }, 300);


        for(UUID uuid: arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            player.setHealth(20);
            player.setFoodLevel(20);
            alivePlayers.add(uuid);
            for(PotionEffect effect: player.getActivePotionEffects()) { player.removePotionEffect(effect.getType()); }
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000000, 10, true, false, false));
            player.setGameMode(GameMode.SURVIVAL);
            player.closeInventory();
        }

        for(UUID uuid: arena.getKits().keySet()) {
            arena.getKits().get(uuid).onStart(Bukkit.getPlayer(uuid));
        }
    }

    @Override
    public boolean isTeamGame() {
        return false;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        //List<UUID> pls = alivePlayers;

        if (arena.getPlayers().contains(e.getEntity().getUniqueId())) {
            if (arena.getState() == GameState.LIVE) {
                arena.sendMessage(ChatColor.AQUA + e.getEntity().getDisplayName() + ChatColor.RESET + " has been eliminated!");
                e.getEntity().setGameMode(GameMode.ADVENTURE);

                alivePlayers.remove(e.getEntity().getUniqueId());
                if (alivePlayers.size() == 1) {
                    arena.clearInventory();
                    lastDead = e.getEntity();
                    winner = Bukkit.getPlayer(alivePlayers.get(0));
                }
            }
        }

        e.getEntity().getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        e.getEntity().getWorld().setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent e) {
        if(arena.getPlayers().size() < alivePlayers.size()) {
            alivePlayers.removeIf(uuid -> !arena.getPlayers().contains(uuid));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        // TODO: REMOVE
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(!(e.getBlock().getType() == Material.SNOW_BLOCK)) {
            if(arena.getPlayers().contains(e.getPlayer().getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(!ConfigManager.getGameSubtype(arena.getId()).equals(GameSubtype.SPLEEF_OLD)) {
            Region region = new Region(ConfigManager.getCorners(arena.getId()).get(0), ConfigManager.getCorners(arena.getId()).get(1));
            if (!region.isLocationInRegion(e.getBlock().getLocation())) {
                e.setCancelled(true);
            }
        }
    }

    void resetArena(boolean afterGame) {
        World world = arena.getWorld();
        GameSubtype gameSubtype = ConfigManager.getGameSubtype(arena.getId());

        if(gameSubtype.equals(GameSubtype.SPLEEF_OLD)) {
            for (int y = 63; y < 71; y++) {
                for (int i = -12; i < 11; i++) {
                    System.out.println("1st for executed");
                    for (int j = -22; j < 21; j++) {
                        System.out.println("setting spleef block");

                        Block block = world.getBlockAt(j, y, i);
                        block.setType(Material.AIR);
                    }
                }
            }

            for (int i = -10; i < 9; i++) {
                System.out.println("1st for executed");
                for (int j = -20; j < 19; j++) {
                    System.out.println("setting spleef block");

                    Block block = world.getBlockAt(j, 63, i);
                    block.setType(Material.SNOW_BLOCK);
                }
            }


            if (afterGame) {
                for (int i = -12; i < 11; i++) {
                    System.out.println("1st for executed");
                    for (int j = -22; j < 21; j++) {
                        System.out.println("setting spleef block");

                        Block block = world.getBlockAt(j, 63, i);
                        block.setType(Material.BARRIER);
                    }
                }
            }
        } else {
            Location lowerCorner = ConfigManager.getSingleCorner(arena.getId(),CornerType.LOWER);
            Location higherCorner = ConfigManager.getSingleCorner(arena.getId(),CornerType.HIGHER);

            Region region = new Region(lowerCorner,higherCorner);
            region.fillRegion(Material.AIR);
            region.fillRegionAtSetY(Material.SNOW_BLOCK, (int) region.getLowCorner().getY());
        }
    }

    @EventHandler
    void onTakeDamage(EntityDamageEvent e) {
        if(e.getEntity().getType().equals(EntityType.PLAYER) && (arena.getState().equals(GameState.LIVE) || arena.getState().equals(GameState.ENDED))) {
            Player player = (Player) e.getEntity();
            if(arena.getPlayers().contains(player.getUniqueId())) {
                if(e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                    alivePlayers.remove(player.getUniqueId());
                    player.setHealth(20);
                    lastDead =player;

                    // if state is live test for win
                    if(arena.getState().equals(GameState.LIVE)) {
                        if (alivePlayers.size() == 1) {
                            if (lastDead.getName().equals(player.getName())) {
                                winner=Bukkit.getPlayer(alivePlayers.get(0));
                                giveWin(winner);
                            }
                        }
                        player.teleport(arena.getRandomSpawnLocation());
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                } else {
                    //cancel damage
                    e.setDamage(0);
                }
            }
        }
    }
}
