// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.mineshaft.minigame.command;

import com.mineshaft.minigame.GameState;
import com.mineshaft.minigame.Minigame;
import com.mineshaft.minigame.instance.Arena;
import com.mineshaft.minigame.instance.GameType;
import com.mineshaft.minigame.instance.team.TeamUI;
import com.mineshaft.minigame.kit.KitUI;
import com.mineshaft.minigame.ui.GameSelectorUI;
import com.mineshaft.minigame.ui.MainUI;
import com.mineshaft.minigame.ui.StatisticsUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    private final Minigame minigame;

    public ArenaCommand(Minigame minigame){
        this.minigame = minigame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if(sender.hasPermission("minigame.admin")) {
                Minigame.getInstance().reloadConfig();
                Minigame.getInstance().reload();
                sender.sendMessage(ChatColor.AQUA + "Config files have been reloaded!");
            } else {
                sender.sendMessage(ChatColor.RED + "You have insufficient permissions to run this command!");
            }
        } else if(sender instanceof Player){
            Player player = (Player) sender;

            if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.GREEN + "These are the available arenas");
                System.out.println("These are the available arenas");

                for(Arena arena : minigame.getArenaManager().getArenas()) {

                    int playerCount;
                    if(arena.getPlayers()!= null){
                        playerCount = arena.getPlayers().size();
                    } else playerCount = 0;

                    // may be errors, may require toString()
                    System.out.println("-" + arena.getId() + " (" + arena.getState().name() + ")");
                    GameType gameType = arena.getGameType();
                    String gameName = gameType.getDisplayName();
                    String endln;
                    if(arena.getPlayers().size() != 1) {endln = "s.";} else {endln = ".";}

                    if(arena.getState() == GameState.RECRUITING){
                        player.sendMessage(ChatColor.GREEN + "-" + arena.getId() + " " + gameName + " (" + arena.getState().name() + "), " + playerCount + " player" + endln);
                    } else if(arena.getState() == GameState.COUNTDOWN) {
                        player.sendMessage(ChatColor.YELLOW + "-" + arena.getId() + " " + gameName + " (" + arena.getState().name() + "), " + playerCount + " player" + endln);
                    } else if(arena.getState() == GameState.ENDED) {
                        player.sendMessage(ChatColor.GOLD + "-" + arena.getId() + " " + gameName + " (" + arena.getState().name() + "), " + playerCount + " player" + endln);
                    } else {
                        player.sendMessage(ChatColor.RED + "-" + arena.getId() + " " + gameName + " (" + arena.getState().name() + ")");
                    }

                }
            } else if(args.length == 1 && args[0].equalsIgnoreCase("kit")) {
                Arena arena = minigame.getArenaManager().getArena(player);
                if(arena != null) {

                    if(arena.getGameType().hasKits()) {

                        if(arena.getState() != GameState.LIVE && arena.getState() != GameState.ENDED) {
                            new KitUI(player, minigame,arena.getGameType());
                        } else {
                            player.sendMessage(ChatColor.RED + "You cannot select a kit at this time");
                        }
                    } else { player.sendMessage("The game you are playing does not allow kit selection!");}

                } else {
                    player.sendMessage(ChatColor.RED + "You are not in an arena");
                }
            } else if(args.length==1 && args[0].equalsIgnoreCase("team")) {
                Arena arena = minigame.getArenaManager().getArena(player);
                if(arena != null) {
                    if(arena.isTeamGame()) {
                        new TeamUI(player, minigame, arena.getGameType());
                    } else {
                        player.sendMessage(ChatColor.RED + "Game does not allow team selection");
                    }
                } else {
                    player.sendMessage(ChatColor.RED+ "You are not in an arena");
                }
            } else if(args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                Arena arena = minigame.getArenaManager().getArena(player);
                if(arena != null) {
                    player.sendMessage(ChatColor.RED + "You left the arena");
                    arena.removePlayer(player);
                } else {
                    player.sendMessage(ChatColor.RED + "You are not in an arena");
                }
            } else if(args.length == 2 && args[0].equalsIgnoreCase("join")) {
                if(minigame.getArenaManager().getArena(player) != null) {
                    player.sendMessage(ChatColor.RED + "You are already playing in an arena");
                    return false;
                }

                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch(NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "You specified an invalid arena ID.");
                    return false;
                }

                if(id >= 0 && id < minigame.getArenaManager().getArenas().size()) {
                    Arena arena = minigame.getArenaManager().getArena(id);
                    if(arena.getState() == GameState.RECRUITING || arena.getState() == GameState.COUNTDOWN) {

                        // if player can join and player count is less than maximum player count
                        if(arena.canJoin() && arena.getPlayers().size()<arena.getMaxPlayers()) {

                            // add player to arena
                            player.sendMessage(ChatColor.GREEN + "You are now playing in arena " + id);
                            arena.addPlayer(player);
                            player.setFireTicks(0);

                        } else {
                            player.sendMessage(ChatColor.RED + "You cannot join the arena right now. Map is still loading.");
                            System.out.println(player + " tried to join unavailable arena: " + id);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You cannot join the arena right now.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You specified an invalid arena ID.");
                }
            } else if(args.length == 1 && (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("shop"))) {
                Arena arena = minigame.getArenaManager().getArena(player);
                if(arena == null || !arena.getState().equals(GameState.LIVE)) {
                    new MainUI(player,minigame);
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot run this command while playing in an arena");
                }
            } else if(args.length == 1 && (args[0].equalsIgnoreCase("profile") || args[0].equalsIgnoreCase("stats")  || args[0].equalsIgnoreCase("statistics"))) {
                Arena arena = minigame.getArenaManager().getArena(player);
                if(arena == null || !arena.getState().equals(GameState.LIVE)) {
                    StatisticsUI.openStatisticsUI(player,minigame);
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot run this command while playing in an arena");
                }
            } else if(args.length== 1 && (args[0].equalsIgnoreCase("ui")||args[0].equalsIgnoreCase("gui"))) {
                Arena arena = minigame.getArenaManager().getArena(player);
                if(arena == null || !arena.getState().equals(GameState.LIVE)) {
                    GameSelectorUI.openMainGameSelectorUI(player,minigame);
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot run this command while playing in an arena");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Invalid usage! These are the options:");
                player.sendMessage(ChatColor.RED + "- /arena list");
                player.sendMessage(ChatColor.RED + "- /arena leave");
                player.sendMessage(ChatColor.RED + "- /arena join <id>");
                player.sendMessage(ChatColor.RED + "- /arena kit");
                player.sendMessage(ChatColor.RED + "- /arena team");
                player.sendMessage(ChatColor.RED + "- /arena menu|shop");
                player.sendMessage(ChatColor.RED + "- /arena ui|gui");
                player.sendMessage(ChatColor.RED + "- /arena profile|stats|statistics");
                player.sendMessage(ChatColor.RED + "- /arena reload");
                player.sendMessage(ChatColor.RED + "- /lobby");
            }
        }


        return false;
    }
}
