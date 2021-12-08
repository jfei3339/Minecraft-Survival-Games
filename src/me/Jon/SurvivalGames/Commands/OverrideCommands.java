package me.Jon.SurvivalGames.Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class OverrideCommands implements Listener, CommandExecutor  {
	
	public String cmd1 = "plugin";
	public String cmd2 = "pl";
	public String cmd3 = "version";
	public String cmd4 = "ver";
	public String cmd5 = "help";
	public String cmd6 = "?";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase(cmd1) || cmd.getName().equalsIgnoreCase(cmd2) || cmd.getName().equalsIgnoreCase(cmd3) || cmd.getName().equalsIgnoreCase(cmd4)
					|| cmd.getName().equalsIgnoreCase(cmd5) ||cmd.getName().equalsIgnoreCase(cmd6) && player.getGameMode().equals(GameMode.SURVIVAL)) 
			
			{
				player.sendMessage("Command not available");
			}
			
		}
			
			
		
		
		
		
		return true;
	}

}
