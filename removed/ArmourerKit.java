// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3


package com.mineshaft.minigame.kit.type;

import com.mineshaft.minigame.Minigame;
import com.mineshaft.minigame.kit.Kit;
import com.mineshaft.minigame.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class ArmourerKit extends Kit {
    public ArmourerKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.ARMORER, uuid);
    }

    @Override
    public void onStart(Player player) {
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        assert swordMeta != null;
        swordMeta.setUnbreakable(true);
        sword.setItemMeta(swordMeta);

        player.getInventory().addItem(sword);
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 5));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
    }
}
