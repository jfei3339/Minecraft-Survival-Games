package me.Jon.SurvivalGames.Mechanics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;

/*
 * Class that disables certain undesirable Minecraft fire-related mechanics to preserve the map.
 */
public class AntiFireSpread implements Listener {
	
	/**
	 * Prevents fire from spreading.
	 * 
	 * @param e: event in which a block spreads to neighboring blocks
	 */
	@EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
    	e.setCancelled(true);        
    }
	
	/**
	 * Prevents fire from breaking blocks.
	 * 
	 * @param e: event in which fire breaks (burns) a block.
	 */
	@EventHandler
	public void onFireBreak(BlockBurnEvent e) {
		e.setCancelled(true);
	}

	
}
