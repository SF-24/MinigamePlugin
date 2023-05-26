package com.xpkitty.minigame.manager.statistics;

public enum StatisticType {

    WINS("Wins"),
    KILLS("Kills"),
    FINAL_KILLS("Final kills"),
    BEDS_BROKEN("Beds Broken");

    private final String name;

    StatisticType(String name) {
        this.name=name;
    }

    public String getName() {return name;}
}
