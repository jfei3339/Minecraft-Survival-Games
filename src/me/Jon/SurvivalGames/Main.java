package me.Jon.SurvivalGames;

import java.io.File;
import java.sql.SQLException;


import org.bukkit.Bukkit;

import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


import me.Jon.SurvivalGames.Events.Chat;
import me.Jon.SurvivalGames.Events.ChestOpenEvent;
import me.Jon.SurvivalGames.Events.InGameEvents;
import me.Jon.SurvivalGames.Events.LobbyEvents;
import me.Jon.SurvivalGames.Events.PreEvents;
import me.Jon.SurvivalGames.Events.SQLevents;
import me.Jon.SurvivalGames.Events.SpecEvents;
import me.Jon.SurvivalGames.Events.Tier2Event;
import me.Jon.SurvivalGames.Mechanics.AntiFireSpread;
import me.Jon.SurvivalGames.SQL.MySQL;
import me.Jon.SurvivalGames.SQL.MySQL_status;
import me.Jon.SurvivalGames.SQL.SQLGetter;
import me.Jon.SurvivalGames.SQL.SQLGetter_status;
import me.Jon.SurvivalGames.Commands.AdminCommands;
import me.Jon.SurvivalGames.Commands.InGameCommands;
import me.Jon.SurvivalGames.Commands.VoteCommand;
import me.Jon.SurvivalGames.Commands.OverrideCommands;
import me.Jon.SurvivalGames.Commands.MapCommands;
import me.Jon.SurvivalGames.Data.MapSpawns;
import me.Jon.SurvivalGames.Data.MapTier2;
import me.Jon.SurvivalGames.Data.ServerInfo;
import net.md_5.bungee.api.ChatColor;

/*
 * Main class that controls the game.
 */
public class Main extends JavaPlugin implements Listener{
	
	//register commands implemented for server
	private final VoteCommand voteCommands = new VoteCommand();
	private final MapCommands mapCommands = new MapCommands();
	private final InGameCommands inGameCommands = new InGameCommands();
	private final OverrideCommands overrideCommands = new OverrideCommands();
	private final AdminCommands adminCommands = new AdminCommands();

	//server variables
	public static final String serverIP = ChatColor.AQUA + "" + ChatColor.BOLD + "By Jonathan Fei";
	public static final  String prefix = ChatColor.DARK_GRAY + StringFunctions.surround("SG", ChatColor.GOLD + "");
	public static String serverName;
	
	//server/map information
	private MapSpawns mapSpawns;
	private MapTier2 mapTier2;
	private ServerInfo serverInfo;
	
	//SQL
	public MySQL SQL;
	public static SQLGetter playerData;
	
	public MySQL_status SQL_status;
	public SQLGetter_status statusData;
	public static boolean connectedToPlayerDB = false;
	public static boolean connectedToStatusDB = false;
	
	public static Game game;
	
	/**
	 * Registers commands, connects to SQL server, registers events, and starts the game when the server is launched.
	 */
	public void onEnable() {
		
		//get map locations
		loadData();
		
		//get rid of internal Minecraft data, removes weird bugs inherent to this version of Minecraft and not this server.
		File playerFilesDir = new File("lobby/playerdata");
		if(playerFilesDir.isDirectory()){
			String[] playerDats = playerFilesDir.list();
			for (int i = 0; i < playerDats.length; i++) {
				File datFile = new File(playerFilesDir, playerDats[i]); 
				datFile.delete();
			} 
		}
		
		this.SQL = new MySQL();
		playerData = new SQLGetter(this);
		
		this.SQL_status = new MySQL_status();
		statusData = new SQLGetter_status(this);
		
		try {
			SQL.connect();
		} catch (ClassNotFoundException | SQLException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "\n Database NOT connected to player database.\n");
		}
		
		try {
			SQL_status.connect();
		} catch (ClassNotFoundException | SQLException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "\n Database NOT connected to status database. \n");
		}
		
		if (SQL.isConnected()) {
			connectedToPlayerDB = true;
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "\n\n\n Database IS connected to player database\n\n\n");
			playerData.createDataTable(); //create tables if it don't exist
			playerData.createInfoTable();
			getServer().getPluginManager().registerEvents(new SQLevents(), this); //register SQL events
		}
		
		if (SQL_status.isConnected()) {
			connectedToStatusDB = true;
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "\n\n\n Database IS connected to status database\n\n\n");
			statusData.createStatusTable(); //create table if it doesn't exist
		}
		
		//Console will say plugin has been enabled
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n SG has been Enabled \n");
		
		//register events
		getServer().getPluginManager().registerEvents(new LobbyEvents(), this);
		getServer().getPluginManager().registerEvents(new PreEvents(), this);
		getServer().getPluginManager().registerEvents(new InGameEvents(), this);
		getServer().getPluginManager().registerEvents(new Chat(), this);
		getServer().getPluginManager().registerEvents(new AntiFireSpread(), this);
		getServer().getPluginManager().registerEvents(new SpecEvents(), this);
		getServer().getPluginManager().registerEvents(new ChestOpenEvent(), this);
		getServer().getPluginManager().registerEvents(new Tier2Event(), this);
		
		//register commands
		this.getCommand(voteCommands.cmd1).setExecutor(voteCommands);
		this.getCommand(voteCommands.cmd2).setExecutor(voteCommands);
		
		this.getCommand(mapCommands.cmd1).setExecutor(mapCommands);
		
		this.getCommand(inGameCommands.cmd1).setExecutor(inGameCommands);
		this.getCommand(inGameCommands.cmd2).setExecutor(inGameCommands);
		this.getCommand(inGameCommands.cmd4).setExecutor(inGameCommands);
		this.getCommand(inGameCommands.cmd5).setExecutor(inGameCommands);
		this.getCommand(inGameCommands.cmd6).setExecutor(inGameCommands);
		this.getCommand(inGameCommands.cmd7).setExecutor(inGameCommands);
		this.getCommand(inGameCommands.cmd8).setExecutor(inGameCommands);
		this.getCommand(inGameCommands.cmd9).setExecutor(inGameCommands);
		
		this.getCommand(overrideCommands.cmd1).setExecutor(overrideCommands);
		this.getCommand(overrideCommands.cmd2).setExecutor(overrideCommands);
		this.getCommand(overrideCommands.cmd3).setExecutor(overrideCommands);
		this.getCommand(overrideCommands.cmd4).setExecutor(overrideCommands);
		this.getCommand(overrideCommands.cmd5).setExecutor(overrideCommands);
		this.getCommand(overrideCommands.cmd6).setExecutor(overrideCommands);
		
		this.getCommand(adminCommands.cmd1).setExecutor(adminCommands);
		
		//initialize and start game
		game = new Game(prefix, playerData);
		game.start(this);
		
;	}
	

	/**
	 * Loads in the map data, such as chest locations and spawns
	 */
	public void loadData() {
		mapSpawns = new MapSpawns();
		mapSpawns.setupMapSpawns();
		mapTier2 = new MapTier2();
		mapTier2.setupMapTier2();
		serverInfo = new ServerInfo();
		serverInfo.setupConfig();
		serverName = ServerInfo.ServerConfig.getString("serverName");
	}
	
	/**
	 * Resets certain offline player data.
	 */
	public void onDisable() {
		
		if (connectedToStatusDB) {
			statusData.setStat(serverName, "STATUS", "RESTARTING");
		}
		
		if (game.getWinningMap() != null) {
			Bukkit.getServer().unloadWorld(game.getWinningMap(), true);
		}
		
		Bukkit.broadcastMessage(prefix + ChatColor.LIGHT_PURPLE + "Trying to delete player data");

		File playerFilesDir = new File("lobby/playerdata");
		if(playerFilesDir.isDirectory()){
			String[] playerDats = playerFilesDir.list();
			for (int i = 0; i < playerDats.length; i++) {
				File datFile = new File(playerFilesDir, playerDats[i]); 
				datFile.delete();
			} 
		}
		
		
		SQL.disconnect();
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n SG has been Disabled \n");
	}
	
    //Unload maps, to rollback maps. Will delete all player builds until last server save
    public static void unloadMap(String mapname){
        if (Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false)){
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "SUCCESSFULLY UNLOADED " + mapname);
        } else {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "COULD NOT UNLOAD " + mapname);
        }
    }
    
    //Load maps (MUST BE CALLED AFTER UNLOAD MAPS TO FINISH THE ROLLBACK PROCESS)
    public static void loadMap(String mapname){
        Bukkit.getServer().createWorld(new WorldCreator(mapname));
    }
 
    //Map rollback method
    public static void rollback(String mapname){
        unloadMap(mapname);
        loadMap(mapname);
    }
	
	
	
    
	


}
