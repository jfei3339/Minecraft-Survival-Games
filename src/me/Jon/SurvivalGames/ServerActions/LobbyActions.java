package me.Jon.SurvivalGames.ServerActions;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/*
 * Class that represents what the server does to a player when they join the server.
 */
public class LobbyActions {
	
	/**
	 * Clears the player of any items, armor, health, etc. they may have had from previously joining the server.
	 * 
	 * @param player: the player who joined
	 */
	public static void clearPlayer(final Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setLevel(0);
        player.setFoodLevel(20);

        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
        if (player.hasPotionEffect(PotionEffectType.POISON)) {
            player.removePotionEffect(PotionEffectType.POISON);
        }
        if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
        }
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        if (player.hasPotionEffect(PotionEffectType.HARM)) {
            player.removePotionEffect(PotionEffectType.HARM);
        }
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }
        if (player.hasPotionEffect(PotionEffectType.SATURATION)) {
            player.removePotionEffect(PotionEffectType.SATURATION);
        }
        if (player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        }
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        player.setHealth(20.0);
        player.setFireTicks(0);
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.SURVIVAL);
    }

}
