package me.Jon.SurvivalGames.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import me.Jon.SurvivalGames.SGScoreboards;
import me.Jon.SurvivalGames.ServerActions.LobbyActions;
import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.Main.GameState;
import me.Jon.SurvivalGames.PlayersSpecs;
import net.md_5.bungee.api.ChatColor;

/*
 * Class managing the logistics of a player joining the server while still waiting for the game to start.
 */
public class LobbyEvents implements Listener{
	
	@EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent e){ //kick players
		
		if (Bukkit.getServer().getMaxPlayers() <= PlayersSpecs.players.size()) {
			Player p = e.getPlayer();
			String rank = Main.data.getRank(p.getUniqueId());
			
			if (!rank.equals("NONE")) {
				for (Player player: PlayersSpecs.players) {
					if (PlayersSpecs.nameColors.get(player).equals(ChatColor.DARK_GREEN + "")) {
						player.kickPlayer("You have been kicked to make room for a donor! Ranks may be purchased at " + ChatColor.GREEN + "store.sunlitmc.net");
						e.allow();
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player p = event.getPlayer();
		
		//join message, holds regardless of everything else (spec join event)
		
		if (Main.connected == true && Main.gameState != GameState.CLEANUP) {
			
			Main.data.dataCreatePlayer(p);
			Main.data.infoCreatePlayer(p);
			PlayersSpecs.playersXP.put(p, 0);
			PlayersSpecs.playersKills.put(p, 0);
			PlayersSpecs.playersChests.put(p, 0);
			
			String rank = Main.data.getRank(p.getUniqueId());
			
			if (rank.equals("OWNER")) {
				PlayersSpecs.nameColors.put(p, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD);
				p.setAllowFlight(true);
			} else if (rank.equals("ADMIN")) {
				PlayersSpecs.nameColors.put(p, ChatColor.DARK_RED + "" + ChatColor.BOLD);
				p.setAllowFlight(true);
			} else if (rank.equals("SRMOD")) {
				PlayersSpecs.nameColors.put(p, ChatColor.DARK_RED + "");
				p.setAllowFlight(true);
			} else if (rank.equals("MOD")) {
				PlayersSpecs.nameColors.put(p, ChatColor.RED + "");
				p.setAllowFlight(true);
			} else if (rank.equals("DIAMOND")) {
				PlayersSpecs.nameColors.put(p, ChatColor.DARK_AQUA + "");
				p.setAllowFlight(true);
			} else if (rank.equals("GOLD")) {
				PlayersSpecs.nameColors.put(p, ChatColor.GOLD + "");
				p.setAllowFlight(true);
			} else if (rank.equals("SILVER")) {
				PlayersSpecs.nameColors.put(p, ChatColor.GRAY + "");
				p.setAllowFlight(true);
			} else {
				PlayersSpecs.nameColors.put(p, ChatColor.DARK_GREEN + "");
			}
			
			if (Main.gameState != GameState.CLEANUP) {
				Bukkit.broadcastMessage(PlayersSpecs.nameColors.get(p) + p.getDisplayName() + ChatColor.GOLD + " has joined.");
			}
			
		} else {
			PlayersSpecs.nameColors.put(p, ChatColor.DARK_GREEN + "");
		}

		if (Main.gameState.equals(GameState.LOBBY)) {
			
			
			LobbyActions.clearPlayer(p);
			
			p.teleport(new Location(Bukkit.getWorld("lobby"), 43.0, 66.0, -30.0, -90, 0));
			
			PlayersSpecs.players.add(p);
			SGScoreboards.createScoreboard(p);		
			p.sendMessage(Main.prefix + ChatColor.GREEN + "A minimum of "  + ChatColor.RED + Main.minPlayers + ChatColor.GREEN + " players is required to start the game.");
			p.sendMessage(Main.prefix + ChatColor.GREEN + "Use /v to view the maps and /v # to vote for a map!");
			
			
			//p.performCommand("v");
		}
		
			
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {	
		
		if (Main.gameState.equals(GameState.LOBBY)) {
			Player player = event.getPlayer();
			PlayersSpecs.players.remove(player);
			Bukkit.broadcastMessage(PlayersSpecs.nameColors.get(player) + player.getDisplayName() + ChatColor.GOLD + " has left.");
		}
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (Main.gameState.equals(GameState.LOBBY) || Main.gameState.equals(GameState.PREGAME) || Main.gameState.equals(GameState.PREDM)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (Main.gameState.equals(GameState.LOBBY) && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (Main.gameState.equals(GameState.LOBBY) && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.setCancelled(true);
		}
	}

}
