package me.Jon.SurvivalGames.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.PlayersSpecs;
import me.Jon.SurvivalGames.StringFunctions;
import net.md_5.bungee.api.ChatColor;

/*
 * Class that determines chat messages
 */
public class Chat implements Listener{
	
	/**
	 * Sends a message in chat from a player.
	 * 
	 * @param event: the event when a player enters something in chat.
	 */
	@EventHandler
	public void chatMsg(AsyncPlayerChatEvent event) {
		Player pl = event.getPlayer();
		
		//change later to account for ranks
		String player;
		if (PlayersSpecs.nameColors.containsKey(pl)) {
			player = PlayersSpecs.nameColors.get(pl) + pl.getDisplayName();
		} else {
			player = ChatColor.DARK_GREEN + pl.getDisplayName();
		}
		
		
		if (PlayersSpecs.players.contains(pl)) {
			int lvl = 1; //Main.data.getStat(pl.getUniqueId(), "LEVEL");
			String level = StringFunctions.surround(lvl + "★", ChatColor.YELLOW + "");
			String msg = ChatColor.WHITE + event.getMessage();
			
			Bukkit.broadcastMessage(level + player + ": " + msg);
			event.setCancelled(true);
		} else {
			String spec = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "SPEC" + ChatColor.GRAY + "] ";
			String msg = ChatColor.WHITE + event.getMessage();
			
			for (Player p: PlayersSpecs.spectators) {
				p.sendMessage(spec + player + ": " + msg);
			}
			
			event.setCancelled(true);
		}
	}

}
