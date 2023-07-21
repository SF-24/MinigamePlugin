// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance.game;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.Game;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ArenaManager;
import com.xpkitty.minigame.manager.statistics.StatisticType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PVPGame extends Game {

    private static FileConfiguration config;

    HashMap<UUID,Boolean> deadPlayers = new HashMap<>();
    HashMap<UUID,Integer> playerKills = new HashMap<>();
    ArrayList<UUID> alivePlayers = new ArrayList<>();
    Minigame minigame;
    ConnectListener connectListener;

    int coinsForWin = 10;
    Player lastdead;
    Player winner;


    public PVPGame(Minigame minigame, Arena arena, ConnectListener connectListener) {
        super(minigame, arena, connectListener);

        this.connectListener = connectListener;
        this.minigame = minigame;
    }

    public void giveKill(Player player, String name) {
        int coinsPerKill = 5;

        if(playerKills.containsKey(player.getUniqueId())) {
            PlayerDataSave dataSave = new PlayerDataSave(player, minigame);
            player.sendMessage(ChatColor.GOLD + "+" + coinsPerKill + " coins"); //error - winner is NULL
            dataSave.addPoints(player, GameType.PVP, coinsPerKill);
            System.out.println("added " + coinsPerKill + " PVP coins to " + player.getDisplayName());
            dataSave.getPlayerJsonDataSave().addStatisticForLatestSeason(GameType.PVP,player,StatisticType.KILLS);


            int kills = playerKills.get(player.getUniqueId());
            playerKills.remove(player.getUniqueId());
            playerKills.put(player.getUniqueId(), kills+1);
            player.sendMessage(ChatColor.AQUA + "You killed " + ChatColor.GOLD + ChatColor.GOLD + name + ChatColor.AQUA + "!");
        }
    }

    public void giveWin(Player player) {

        for(UUID uuid : arena.getPlayers()) {
            Player uuidPlayer = Bukkit.getPlayer(uuid);
            uuidPlayer.setHealth(20);
            uuidPlayer.setFoodLevel(20);
            for(PotionEffect effect: uuidPlayer.getActivePotionEffects()) { uuidPlayer.removePotionEffect(effect.getType()); }
        }

        winner = player;

        arena.setState(GameState.ENDED);
        PlayerDataSave dataSave = new PlayerDataSave(winner, minigame);

        winner.sendMessage(ChatColor.GOLD + "+" + coinsForWin + " coins");


        dataSave.addPoints(winner ,GameType.PVP, coinsForWin);
        dataSave.getPlayerJsonDataSave().addStatisticForLatestSeason(GameType.PVP,player, StatisticType.WINS);

        System.out.println("added " + coinsForWin + " PVP coins to " + winner.getDisplayName());

        winner.sendTitle(ChatColor.GREEN + "VICTORY","",10,100,10);
        player.sendMessage(ChatColor.RED + "VICTORY! YOU WIN!");
        arena.sendMessage(ChatColor.GOLD + player.getName() + " HAS WON with " + playerKills.get(player.getUniqueId()) + " kills! Thanks for playing.");
        arena.clearInventory();
        arena.sendMessage("");

        HashMap<String, Integer> playerKillsByName = new HashMap<>();
        int playerKillsLength = 0;

        List<String> topKills;

        for(UUID uuid : playerKills.keySet()) {
            Player uuidPlayer = Bukkit.getPlayer(uuid);
            assert uuidPlayer!=null;
            String name = uuidPlayer.getName();

            playerKillsByName.put(name, playerKills.get(uuid));
            playerKillsLength++;
        }

        if(playerKillsLength > 2) {
            topKills = ArenaManager.firstN(playerKillsByName, 3);
        } else {
            topKills = ArenaManager.firstN(playerKillsByName, 2);
        }


        arena.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + " TOP KILLS:");

        topKills.forEach( (playerName) -> arena.sendMessage(playerName + " got " + playerKillsByName.get(playerName) + " kills") );

        Bukkit.getScheduler().runTaskLater(minigame, () -> {
            arena.sendMessage(ChatColor.GOLD + "Game has ended. You have been moved to lobby");
            arena.sendTitle("","");
            arena.reset(false, 0);
        }, 300);


    }

    @Override
    public void onStart() {
        arena.sendMessage(ChatColor.GREEN + "GAME HAS STARTED! The last person alive wins. Good luck!");
        arena.clearInventory();

        for(UUID uuid: arena.getPlayers()) {
            alivePlayers.add(uuid);

            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            player.setHealth(20);
            player.setFoodLevel(20);
            playerKills.put(uuid, 0);
            for(PotionEffect effect: player.getActivePotionEffects()) { Bukkit.getPlayer(uuid).removePotionEffect(effect.getType()); }
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
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntityType().equals(EntityType.PLAYER)) {

            Player player = (Player) e.getEntity();
            player.getHealth();
        }
    }

    /*@EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntityType().equals(EntityType.PLAYER)) {

            Player killedPlayer = (Player) e.getEntity();
            if(killedPlayer.getHealth()<1) {
                e.setCancelled(true);
                killedPlayer.teleport(arena.getRespawn());
                killedPlayer.setHealth(20);
                killedPlayer.setFoodLevel(20);

                if(alivePlayers.contains(e.getEntity().getUniqueId())) {
                    alivePlayers.remove(e.getEntity().getUniqueId());

                    if(e instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent en = (EntityDamageByEntityEvent) e;

                        if(arena.getPlayers().contains((en).getEntity().getUniqueId()) && arena.getPlayers().contains(en.getDamager().getUniqueId())) {
                            if(arena.getState() == GameState.LIVE) {

                                Player player = (Player) en.getDamager();
                                arena.sendMessage(ChatColor.AQUA + killedPlayer.getDisplayName() + ChatColor.RESET + " has been killed by " + ChatColor.RED + player.getDisplayName() + "!");
                                System.out.println(player.getName() + " killed " + killedPlayer.getName());


                                giveKill(player, player.getName());
                            }
                        }
                    } else {
                        arena.sendMessage(ChatColor.AQUA + killedPlayer.getDisplayName() + ChatColor.RESET + " has been eliminated!");
                    }
                    if(alivePlayers.size() == 1) {
                        arena.clearInventory();
                        lastdead = killedPlayer;
                        winner = Bukkit.getPlayer(alivePlayers.get(0));
                        giveWin(winner);
                    }

                }

            }

        }
    }*/

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        //List<UUID> pls = alivePlayers;

        alivePlayers.remove(e.getEntity().getUniqueId());

        e.getEntity().getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
        e.getEntity().getWorld().setGameRule(GameRule.SHOW_DEATH_MESSAGES,false);

        if(e.getEntity().getKiller() != null) {
            if(arena.getPlayers().contains(e.getEntity().getKiller().getUniqueId()) && arena.getPlayers().contains(e.getEntity().getKiller().getUniqueId())) {
                if(arena.getState() == GameState.LIVE) {
                    System.out.println(e.getEntity().getKiller().getName() + " killed " + e.getEntity().getName());

                    Player player = e.getEntity().getKiller();
                    arena.sendMessage(ChatColor.AQUA + e.getEntity().getDisplayName() + ChatColor.RESET + " has been killed by " + ChatColor.RED + e.getEntity().getKiller().getDisplayName() + "!");


                    giveKill(player, e.getEntity().getName());
                }
            }
        } else {
            arena.sendMessage(ChatColor.AQUA + e.getEntity().getDisplayName() + ChatColor.RESET + " has been eliminated!");
        }
        if(alivePlayers.size() == 1) {
            arena.clearInventory();
            lastdead = e.getEntity();
            winner = Bukkit.getPlayer(alivePlayers.get(0));
            giveWin(winner);
        }

        /*if (pls.contains(e.getEntity().getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(minigame, () -> {
                e.getEntity().teleport(arena.getRespawn());
                pls.remove(e.getEntity().getUniqueId());
            }, 20);
        }*/
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent e) {
        if(arena.getPlayers().size() < playerKills.size()) {
            for(UUID uuid: playerKills.keySet()) {
                if(!arena.getPlayers().contains(uuid)) {
                    playerKills.remove(uuid);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if(arena.getState().equals(GameState.LIVE)) {
            Player player = e.getPlayer();

            if (arena.getPlayers().contains(player.getUniqueId())) {
                player.teleport(arena.getRandomSpawnLocation());
                player.setGameMode(GameMode.SPECTATOR);
            }

            if (playerKills.size() == 1) {
                if (lastdead.getName().equals(e.getPlayer().getName())) {
                    giveWin(winner);
                }
            }
        }

    }
}
