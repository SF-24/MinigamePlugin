package com.xpkitty.minigame.instance.data;

import com.xpkitty.minigame.kit.KitType;

import java.util.HashMap;

public class PlayerDataClass {

    public PlayerDataClass(HashMap<CoinType, Integer> coins, HashMap<KitType, Boolean> kits) {
        this.coins=coins;
        this.kits=kits;
    }


    HashMap<CoinType, Integer> coins;
    HashMap<KitType, Boolean> kits;

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
