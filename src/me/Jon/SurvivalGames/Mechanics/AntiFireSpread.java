package me.Jon.SurvivalGames.Mechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;



public class AntiFireSpread implements Listener {
	
	@EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
    	e.setCancelled(true);        
    }
	
	@EventHandler
	public void onFireBreak(BlockBurnEvent e) {
		e.setCancelled(true);
	}

	
}
