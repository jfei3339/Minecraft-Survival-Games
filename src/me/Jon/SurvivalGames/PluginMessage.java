package me.Jon.SurvivalGames;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.Jon.SurvivalGames.Main;

/*
 * Class for Bungeecord. Currently not using.
 */
public class PluginMessage implements PluginMessageListener{
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
	    if (!channel.equals("BungeeCord")) {
	        return;
	      }
	      ByteArrayDataInput in = ByteStreams.newDataInput(message);
	      String subchannel = in.readUTF();

	      
	      if (subchannel.equals("SomeSubChannel")) {
	        // Use the code sample in the 'Response' sections below to read
	        // the data.
	      }
	}
	
	
	public void connect(Player player, String server) {	
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF("Connect");
		output.writeUTF(server);
		
		//Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "" + Main.getInstance());
		player.sendPluginMessage(Main.getInstance(), "BungeeCord", output.toByteArray());
	}

}
