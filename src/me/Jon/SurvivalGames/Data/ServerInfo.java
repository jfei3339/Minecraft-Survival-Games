package me.Jon.SurvivalGames.Data;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Jon.SurvivalGames.Main;

public class ServerInfo {
	
	private Main plugin = Main.getPlugin(Main.class);
	
	//File, FileConfig
	
	public static File Server;
	public static FileConfiguration ServerConfig;
	
	//setup folder
	
	public void setupConfig() {
		
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		Server = new File(plugin.getDataFolder(), "ServerInfo.yml");
		
		if (!Server.exists()) {
			try {
				Server.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The ServerInfo.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender()
						.sendMessage(ChatColor.RED + "Could not create the ServerInfo.yml file");
			}
		}
		
		ServerConfig = YamlConfiguration.loadConfiguration(Server);
		
	}
	
	public static FileConfiguration getStats() {
		
		return ServerConfig;
		
	}
	
	public static void saveData() {
		
		try {
			ServerConfig.save(Server);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The Server.yml file has been saved");

		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the Server.yml file");
		}
		
	}
	
	public static void reloadData() {
		ServerConfig = YamlConfiguration.loadConfiguration(Server);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "The Server.yml file has been reload");

	}

}
