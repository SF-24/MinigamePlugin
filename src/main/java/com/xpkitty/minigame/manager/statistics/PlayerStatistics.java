// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.manager.statistics;

import com.xpkitty.minigame.instance.GameType;

import java.util.HashMap;

public class PlayerStatistics {

    HashMap<GameType, HashMap<StatisticType, Integer>> stats = new HashMap<>();

    public PlayerStatistics() {
        for(GameType gameType : GameType.values()) {
            if(gameType.getStatistics()!=null) {
                // assert false is here to not get warning
                assert false;
                stats.put(gameType, generateEmptyGameStats(gameType));
            }
        }
    }

    // add missing games to main hashmap
    public void updateStats() {
        for(GameType gameType : GameType.values()) {
            if(gameType.getStatistics()!=null && !stats.containsKey(gameType)) {
                stats.put(gameType, generateEmptyGameStats(gameType));
            }
        }
    }

    // generate empty stat hashmap for a game
    public HashMap<StatisticType, Integer> generateEmptyGameStats(GameType gameType) {
        // create and initialize stat hash map for this game
        HashMap<StatisticType, Integer> gameStats = new HashMap<>();

        // loop through stats for this game
        for(StatisticType stat : gameType.getStatistics()) {
            gameStats.put(stat,0);
        }
        System.out.println("debug:" +gameStats);
        return gameStats;
    }

    // get statistics hashmap from game
    public HashMap<StatisticType, Integer> getStatsForGame(GameType gameType) {
        return stats.get(gameType);
    }

    public void setStatsForGame(GameType gameType, HashMap<StatisticType, Integer> stats) {
        this.stats.put(gameType,stats);
    }

    // get a statistic from a game
    public int getStatFromGame(GameType gameType, StatisticType statisticType) {
        if(gameType.getStatistics().contains(statisticType)) {
            HashMap<StatisticType, Integer> stats = getStatsForGame(gameType);
            return stats.get(statisticType);
        }
        return -1;
    }

    public void setStatForGame(GameType gameType, StatisticType statisticType, int value) {
        if(gameType.getStatistics().contains(statisticType)) {
            if(value<0) {
                value=0;
            }

            HashMap<StatisticType, Integer> stats = getStatsForGame(gameType);
            stats.put(statisticType,value);
            setStatsForGame(gameType,stats);
        }
    }

    // add value to statistic
    public void addToStatForGame(GameType gameType, StatisticType statisticType, int value) {
        int i = getStatFromGame(gameType,statisticType);
        i+=value;
        setStatForGame(gameType,statisticType,i);
    }
}
