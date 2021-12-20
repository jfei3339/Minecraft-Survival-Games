package me.Jon.SurvivalGames.Mechanics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
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
