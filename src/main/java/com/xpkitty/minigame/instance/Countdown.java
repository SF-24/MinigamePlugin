package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private final Minigame minigame;
    private final Arena arena;
    private int countdownSeconds;

    public Countdown(Minigame minigame, Arena arena){
        this.minigame = minigame;
        this.arena = arena;
        this.countdownSeconds = ConfigManager.getCountdownSeconds();
    }

    public void start() {

        arena.setState(GameState.COUNTDOWN);
        runTaskTimer(minigame, 0, 20);
    }

    @Override
    public void run() {
        if(countdownSeconds == 0){
            cancel();
            arena.start();
            arena.sendTitle("","");
            return;
        }

        if(countdownSeconds <= 10 || countdownSeconds % 15 == 0){
            arena.sendMessage(ChatColor.GREEN + "Game will start in " + countdownSeconds + " second" + (countdownSeconds == 1 ? "" : "s") + ".");

            if(countdownSeconds > 15) {
                arena.sendTitle(ChatColor.GREEN.toString() + countdownSeconds ,"");
            } else if(countdownSeconds > 10) {
                arena.sendTitle(ChatColor.YELLOW.toString() + countdownSeconds ,"");
            } else {
                arena.sendTitle(ChatColor.RED.toString() + countdownSeconds ,"");
            }

        } else { arena.sendTitle("",""); }

        countdownSeconds--;
    }
}
