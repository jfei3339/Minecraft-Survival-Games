package me.Jon.SurvivalGames.Events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.PlayersSpecs;
import net.md_5.bungee.api.ChatColor;

public class SQLevents implements Listener{
	

	
	
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		
		if (e.getEntity().getKiller() instanceof Player) {
			Player killer = (Player) e.getEntity().getKiller();
			UUID uuid = killer.getUniqueId();

			PlayersSpecs.playersXP.put(killer, PlayersSpecs.playersXP.get(killer) + 10);
			PlayersSpecs.playersKills.put(killer, PlayersSpecs.playersKills.get(killer) + 1);

			killer.sendMessage(ChatColor.LIGHT_PURPLE + "+10 XP (kill)");
			
			/*
			int newLevel = getLevel(killer);
			
			if (newLevel > currLevel) {
				killer.sendMessage(ChatColor.AQUA + "Congratulations! You are now level " + newLevel + "!");
			}
			*/
			
			//Main.data.setStat(killer.getUniqueId(), "LEVEL", newLevel);
			
			/*
			double kills = Main.data.getStat(uuid, "KILLS");
			double deaths = Main.data.getStat(uuid, "LOSSES");
			
			if (deaths == 0) {
				deaths = 1;
			}
			*/
			
			//Main.data.setStat(uuid, "KDR", kills/deaths);
		}
		
	} 
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (Main.gameState.equals("INGAME") || Main.gameState.equals("PREDM") || Main.gameState.equals("DEATHMATCH")) {
			Player player = e.getEntity();
			UUID uuid = player.getUniqueId();
			Main.data.setStat(uuid, "LOSSES", Main.data.getStat(uuid, "LOSSES") + 1);
			
			double kills = Main.data.getStat(uuid, "KILLS");
			double deaths = Main.data.getStat(uuid, "LOSSES");
			
			if (deaths == 0) {
				deaths = 1;
			}
			
			Main.data.setStat(uuid, "KDR", kills/deaths);
		}
	}
	
	//Leveling system
	public static int[] getProgress(Player p) {
		UUID uuid = p.getUniqueId();
		
		int xp = Main.data.getStat(uuid, "XP");
		
		int level = 1;

		int target = 0;
		
		if (xp < 200) {
			level = 1;
		} else if (xp < 700) {
			level = 2;
			target = 700;
		} else if (xp < 1500) {
			level = 3;
			target = 1500;
		} else if (xp < 2500) {
			level = 4;
			target = 2500;
		} else if (xp < 5000) {
			level = 5;
			target = 5000;
		} else if (xp < 10000) {
			level = 6;
			target = 10000;
		} else if (xp < 20000) {
			level = 7;
			target = 20000;
		} else if (xp < 35000) {
			level = 8;
			target = 35000;
		} else if (xp < 65000) {
			level = 9;
			target = 65000;
		} else if (xp < 100000) {
			level = 10;
			target = 100000;
		} else if (xp < 150000) {
			level = 11;
			target = 150000;
		} else if (xp < 250000) {
			level = 12;
		} else {
			level = 13;
			target = 350000;
			while (xp>250000) {
				level++;
				xp -= 100000;
				target += 100000;
			}

		}
		
		int[] arr = {level, target};
		
		return arr;
		
		
	}
}
