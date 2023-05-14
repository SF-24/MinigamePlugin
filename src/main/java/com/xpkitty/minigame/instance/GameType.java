package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.instance.game.PVPGame;

public enum GameType {
    PVP(false),
    SHOVELSPLEEF(false),
    KNOCKOUT(false),
    BEDWARS(true);

    private final boolean isTeamGame;

    GameType(boolean isTeamGame) {
        this.isTeamGame=isTeamGame;
    }

    public boolean isTeamGame() {return isTeamGame;}
}
