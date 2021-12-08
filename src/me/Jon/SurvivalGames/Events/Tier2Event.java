package me.Jon.SurvivalGames.Events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import me.Jon.SurvivalGames.Data.MapTier2;

public class Tier2Event implements Listener{
	
	@EventHandler
	public void makeTier2(BlockBreakEvent event) {
		
		Player p = event.getPlayer();
		String map = p.getWorld().getName();
		
		Block b = event.getBlock();
		
		int chestNum = 1;
		while (MapTier2.mapTier2Config.contains("Maps. " + map + "." + chestNum)) {
			chestNum++;
		}
		
		if (event.getBlock().getType().equals(Material.CHEST) && p.getGameMode().equals(GameMode.CREATIVE)) {
			
			MapTier2.mapTier2Config.set("Maps. " + map + "." + chestNum + ".x", b.getX());
			MapTier2.mapTier2Config.set("Maps. " + map + "." + chestNum + ".y", b.getY());
			MapTier2.mapTier2Config.set("Maps. " + map + "." + chestNum + ".z", b.getZ());
			MapTier2.saveData();
			chestNum++;
			
			
			
			event.getPlayer().sendMessage( String.valueOf(b.getX()) + " " + String.valueOf(b.getY()) + " " + String.valueOf(b.getZ()) + " has been set to a tier 2 chest.");
			
			event.setCancelled(true);
		}
	}

}
