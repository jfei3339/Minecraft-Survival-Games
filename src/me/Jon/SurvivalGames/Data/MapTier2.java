package me.Jon.SurvivalGames.Data;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Jon.SurvivalGames.Main;

public class MapTier2 {
	
	private Main plugin = Main.getPlugin(Main.class);
	
	//File, FileConfig
	
	public static File mapTier2;
	public static FileConfiguration mapTier2Config;
	
	//setup folder
	
	public void setupMapTier2() {
		
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		mapTier2 = new File(plugin.getDataFolder(), "mapTier2.yml");
		
		if (!mapTier2.exists()) {
			try {
				mapTier2.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The mapTier2.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "Could not create the mapTier2.yml file");
			}
		}
		
		mapTier2Config = YamlConfiguration.loadConfiguration(mapTier2);
		
	}
	
	public static FileConfiguration getStats() {
		
		return mapTier2Config;
		
	}
	
	public static void saveData() {
		
		try {
			mapTier2Config.save(mapTier2);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The mapTier2.yml file has been saved");

		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the mapTier2.yml file");
		}
		
	}
	
	public static void reloadData() {
		mapTier2Config = YamlConfiguration.loadConfiguration(mapTier2);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The mapTier2.yml file has been reload");

	}

}
