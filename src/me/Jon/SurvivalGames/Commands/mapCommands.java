package me.Jon.SurvivalGames.Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Jon.SurvivalGames.Data.MapSpawns;

public class MapCommands implements Listener, CommandExecutor {
	
	public String cmd1 = "pos";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase(cmd1) && player.getGameMode().equals(GameMode.CREATIVE)) {
				
				String map = player.getWorld().getName();
				String posNum = args[0];
				
				MapSpawns.mapSpawnsConfig.set("Maps. " + map + ".pos" + posNum + ".x", player.getLocation().getX());
				MapSpawns.mapSpawnsConfig.set("Maps. " + map + ".pos" + posNum + ".y", player.getLocation().getY());
				MapSpawns.mapSpawnsConfig.set("Maps. " + map + ".pos" + posNum + ".z", player.getLocation().getZ());
				MapSpawns.mapSpawnsConfig.set("Maps. " + map + ".pos" + posNum + ".yaw", player.getLocation().getYaw());
				MapSpawns.mapSpawnsConfig.set("Maps. " + map + ".pos" + posNum + ".pitch", player.getLocation().getPitch());
				MapSpawns.saveData();
				
				player.sendMessage("Position " + posNum + " has been set to " + "(" + player.getLocation().getX() + ", " + player.getLocation().getY() + ", "
						+ player.getLocation().getZ() + ")" + player.getLocation().getYaw() + " " + player.getLocation().getPitch()
						);
				
				
			} else {
				
			}
			
		}
		
		return true;
	}

}
