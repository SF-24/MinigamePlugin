// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3


package com.xpkitty.minigame.kit.type;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.kit.Kit;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

public class DefaultSpleefKit extends Kit {

    public DefaultSpleefKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.SPLEEF_DEFAULT, uuid);
    }

    @Override
    public void onStart(Player player) {

        ItemStack snow = new ItemStack(Material.SNOW_BLOCK, 20);
        ItemStack shovel = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemMeta shovelMeta = shovel.getItemMeta();
        shovelMeta.setUnbreakable(true);
        shovelMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        shovelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        shovelMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("damage",-0.99999f, ADD_NUMBER));
        shovel.setItemMeta(shovelMeta);

        player.getInventory().addItem(shovel);
        player.getInventory().addItem(snow);

    }
}
