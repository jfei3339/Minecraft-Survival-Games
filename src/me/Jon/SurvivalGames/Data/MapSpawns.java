package me.Jon.SurvivalGames.Data;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Jon.SurvivalGames.Main;

public class MapSpawns {
	
	private Main plugin = Main.getPlugin(Main.class);
	
	//File, FileConfig
	
	public static File mapSpawns;
	public static FileConfiguration mapSpawnsConfig;
	
	//setup folder
	
	public void setupMapSpawns() {
		
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		mapSpawns = new File(plugin.getDataFolder(), "mapSpawns.yml");
		
		if (!mapSpawns.exists()) {
			try {
				mapSpawns.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The mapSpawns.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "Could not create the mapSpawns.yml file");
			}
		}
		
		mapSpawnsConfig = YamlConfiguration.loadConfiguration(mapSpawns);
		
	}
	
	public static FileConfiguration getStats() {
		
		return mapSpawnsConfig;
		
	}
	
	public static void saveData() {
		
		try {
			mapSpawnsConfig.save(mapSpawns);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The mapSpawns.yml file has been saved");

		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the mapSpawns.yml file");
		}
		
	}
	
	public static void reloadData() {
		mapSpawnsConfig = YamlConfiguration.loadConfiguration(mapSpawns);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The mapSpawns.yml file has been reload");

	}

}
