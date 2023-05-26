package com.xpkitty.minigame.instance.data;

import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.manager.ConfigManager;
import com.xpkitty.minigame.manager.statistics.PlayerStatistics;

import java.util.HashMap;

public class PlayerDataClass {

    public PlayerDataClass(HashMap<CoinType, Integer> coins, HashMap<KitType, Boolean> kits, HashMap<Integer, PlayerStatistics> statistics) {
        this.coins=coins;
        this.kits=kits;
        this.statistics=statistics;
    }


    HashMap<CoinType, Integer> coins;
    HashMap<KitType, Boolean> kits;
    HashMap<Integer, PlayerStatistics> statistics;

    public HashMap<Integer, PlayerStatistics> getStatistics() {
        return statistics;
    }

    public PlayerStatistics getStatisticsForSeason(int season) {
        return statistics.get(season);
    }

    public void updateStatisticHashMap() {
        if(getStatistics()!=null) {
            for (Integer i : statistics.keySet()) {
                PlayerStatistics playerStatistics = statistics.get(i);
                playerStatistics.updateStats();
                statistics.put(i, playerStatistics);
            }
        }
    }

    public void updatePlayerStatistics(PlayerStatistics playerStatistics, int season) {
        statistics.put(season,playerStatistics);
    }

    public void updateLatestPlayerStatistics(PlayerStatistics playerStatistics) {
        updatePlayerStatistics(playerStatistics, ConfigManager.getCurrentSeason());
    }

    private void checkContainsCoinType(CoinType coinType) {
        if(!coins.containsKey(coinType) && coinType!=null) {
            coins.put(coinType,0);
        }
    }

    private void checkContainsKitType(KitType kitType) {
        if(!kits.containsKey(kitType) && kitType!=null) {
            if(kitType.getPrice()<=0) {
                kits.put(kitType, true);
            } else {
                kits.put(kitType,false);
            }
        }
    }

    public void giveKit(KitType kitType) {
        kits.put(kitType, true);
    }

    public int getCoinCount(CoinType coinType) {
        checkContainsCoinType(coinType);
        return coins.get(coinType);
    }

    public boolean getKitStatus(KitType kit) {
        checkContainsKitType(kit);
        return kits.get(kit);
    }

    public void addCoins(CoinType coinType, int amount) {
        checkContainsCoinType(coinType);
        int val = coins.get(coinType) + amount;
        coins.put(coinType, val);
    }
}
