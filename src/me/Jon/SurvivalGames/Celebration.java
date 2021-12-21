package me.Jon.SurvivalGames;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import me.Jon.SurvivalGames.Main.GameState;
import me.Jon.SurvivalGames.Data.MapSpawns;
import me.Jon.SurvivalGames.Data.MapTier2;
import net.md_5.bungee.api.ChatColor;

/*
 * Class that manages logistics after a player wins.
 */
public class Celebration {
	
	/**
	 * Once a winner has been determined, update stats, display celebratory messages, and execute celebratory events such as fireworks.
	 */
	public static void celebrate() {
		
		for (Player p: PlayersSpecs.players) {
			p.getWorld().setTime(14000);
			Bukkit.broadcastMessage(Main.prefix + PlayersSpecs.nameColors.get(p) + p.getDisplayName() + ChatColor.GREEN + " has won the Survival Games!");
			
			if (Main.connectedToPlayerDB == true) {
				Main.playerData.setStat(p.getUniqueId(), "WINS", Main.playerData.getStat(p.getUniqueId(), "WINS") + 1);
				p.sendMessage(ChatColor.LIGHT_PURPLE + "+200 XP (win)");
				Main.playerData.setStat(p.getUniqueId(), "XP", Main.playerData.getStat(p.getUniqueId(), "XP") + 200);

			}
			
			Main.gameState = GameState.CLEANUP;
		}
		
		//fireworks
		
		int posNum = 1;
		String map1 = Main.winningMap;
		while (MapTier2.mapTier2Config.contains("Maps. " + map1 + "." + posNum)) {
			posNum++;
			
			double x = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".x");
			double y = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".y");
			double z = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".z");
			Location loc = new Location(Bukkit.getWorld(map1), x, y, z, 0, 0); 
			
			Bukkit.getWorld(map1).spawnEntity(loc, EntityType.FIREWORK);
			
			
		}
	}
	/**
	 * Get the level of the player on the server.
	 * 
	 * @param player: the player whose level to get
	 * @return the level of the player
	 */
	public static int getLevel(Player player) {
		UUID uuid = player.getUniqueId();
		int xp = Main.playerData.getStat(uuid, "XP");
		int level = 1;
		if (xp < 200) {
			level = 1;
		} else if (xp < 700) {
			level = 2;
		} else if (xp < 1500) {
			level = 3;
		} else if (xp < 2500) {
			level = 4;
		} else if (xp < 5000) {
			level = 5;
		} else if (xp < 10000) {
			level = 6;
		} else if (xp < 20000) {
			level = 7;
		} else if (xp < 35000) {
			level = 8;
		} else if (xp < 65000) {
			level = 9;
		} else if (xp < 100000) {
			level = 10;
		} else if (xp < 150000) {
			level = 11;
		} else if (xp < 250000) {
			level = 12;
		} else {
			level = 13;
			while (xp>250000) {
				level += 1;
				xp -= 100000;
			}
		}
		return level;
		
		
	}

}
