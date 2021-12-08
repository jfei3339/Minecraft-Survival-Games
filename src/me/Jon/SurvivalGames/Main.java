package me.Jon.SurvivalGames;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;


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
import me.Jon.SurvivalGames.Commands.mapCommands;
import me.Jon.SurvivalGames.Data.MapSpawns;
import me.Jon.SurvivalGames.Data.MapTier2;
import me.Jon.SurvivalGames.Data.ServerInfo;
import net.md_5.bungee.api.ChatColor;

/*
 * Main class that controls the plugin.
 */
public class Main extends JavaPlugin implements Listener{
	
	//register commands implemented for server
	private final VoteCommand coms = new VoteCommand();
	private final mapCommands coms2 = new mapCommands();
	private final InGameCommands ingameComs = new InGameCommands();
	private final OverrideCommands coms4 = new OverrideCommands();
	private final AdminCommands coms5 = new AdminCommands();
	
	//this communicates to other servers
	private PluginMessage bungee = new PluginMessage();
	
	//game variables
	public static final String serverIP = ChatColor.AQUA + "" + ChatColor.BOLD + "servername.com";
	public static final  String prefix = ChatColor.DARK_GRAY + StringFunctions.surround("Server", ChatColor.GOLD + "");
	public static String serverName;
	
	private final static int minPlayers = 6;
	
	public static final int lobbyMinTime = 60;
	public static int lobbyTimeLeft = lobbyMinTime;
	
	public static final int preMinTime = 15;
	public static  int preTimeLeft = preMinTime;
	
	final public static int inGameMinTime = 1200;
	public static int inGameTimeLeft = inGameMinTime;
	
	final public static int preDMMinTime = 10;
	public static int preDMTimeLeft = preDMMinTime;
	
	final public static int DMMinTime = 180;
	public static int DMTimeLeft = DMMinTime;
	
	final public static int cleanupMinTime = 10;
	public static int cleanupTimeLeft = cleanupMinTime;
	
	final public static int refill1Time = 600;
	final public static int refill2Time = 120;
	
	enum GameState {
		LOBBY, 
		PREGAME,
		INGAME,
		PREDM,
		DEATHMATCH,
		CLEANUP,
		RESTARTING
		
	}
	
	public static GameState gameState = GameState.LOBBY;
	
	private MapSpawns mp;
	private MapTier2 mt;
	private ServerInfo si;
	
	public MySQL SQL;
	public static SQLGetter data;
	
	public MySQL_status SQL_status;
	public static SQLGetter_status statusdata;
	public static boolean connected = false;
	public int count = 0;
	

	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	private static void setInstance(Main instance) {
		Main.instance = instance;
	}

	
	public void onEnable() {
		
		//get map locations
		loadData();
		
		//Bungee stuff
		setInstance(this);
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage());
		
		//get rid of playerdata, remove weird bugs
		File playerFilesDir = new File("lobby/playerdata");
		if(playerFilesDir.isDirectory()){
		String[] playerDats = playerFilesDir.list();
		for (int i = 0; i < playerDats.length; i++) {
		File datFile = new File(playerFilesDir, playerDats[i]); 
		datFile.delete();
		} }
		
		this.SQL = new MySQL();
		data = new SQLGetter(this);
		
		this.SQL_status = new MySQL_status();
		statusdata = new SQLGetter_status(this);
		
		try {
			SQL.connect();
			SQL_status.connect();
		} catch (ClassNotFoundException | SQLException e) {

			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "\n Database NOT connected \n");
		}
		
		if (SQL.isConnected()) {
			connected = true;
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "\n\n\n Database IS connected \n\n\n");
			
			data.createDataTable(); //create table if it doesn't exist
			data.createInfoTable();
			getServer().getPluginManager().registerEvents(new SQLevents(), this);

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
		this.getCommand(coms.cmd1).setExecutor(coms);
		this.getCommand(coms.cmd2).setExecutor(coms);
		
		this.getCommand(coms2.cmd1).setExecutor(coms2);
		
		this.getCommand(ingameComs.cmd1).setExecutor(ingameComs);
		this.getCommand(ingameComs.cmd2).setExecutor(ingameComs);
		this.getCommand(ingameComs.cmd4).setExecutor(ingameComs);
		this.getCommand(ingameComs.cmd5).setExecutor(ingameComs);
		this.getCommand(ingameComs.cmd6).setExecutor(ingameComs);
		this.getCommand(ingameComs.cmd7).setExecutor(ingameComs);
		this.getCommand(ingameComs.cmd8).setExecutor(ingameComs);
		this.getCommand(ingameComs.cmd9).setExecutor(ingameComs);
		
		this.getCommand(coms4.cmd1).setExecutor(coms4);
		this.getCommand(coms4.cmd2).setExecutor(coms4);
		this.getCommand(coms4.cmd3).setExecutor(coms4);
		this.getCommand(coms4.cmd4).setExecutor(coms4);
		this.getCommand(coms4.cmd5).setExecutor(coms4);
		this.getCommand(coms4.cmd6).setExecutor(coms4);
		
		this.getCommand(coms5.cmd1).setExecutor(coms5);
		
		startSG();
		updateStatus();
		
;	}
	

	
	public void loadData() {
		mp = new MapSpawns();
		mp.setupMapSpawns();
		
		mt = new MapTier2();
		mt.setupMapTier2();
		
		si = new ServerInfo();
		si.setupConfig();
		
		serverName = si.ServerConfig.getString("serverName");
	}
	
	//fired when plugin is disabled
	public void onDisable() {
		gameState = GameState.RESTARTING;
		statusdata.setStat(serverName, "STATUS", "RESTARTING");
		if (winningMap != null) {
			Bukkit.getServer().unloadWorld(winningMap, true);
		}
		
		Bukkit.broadcastMessage(prefix + ChatColor.LIGHT_PURPLE + "Trying to delete player data");

		
		File playerFilesDir = new File("lobby/playerdata");
		if(playerFilesDir.isDirectory()){
		String[] playerDats = playerFilesDir.list();
		for (int i = 0; i < playerDats.length; i++) {
		File datFile = new File(playerFilesDir, playerDats[i]); 
		datFile.delete();
		} }
		
		
		SQL.disconnect();
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n SG has been Disabled \n");
	}
	
	
	
	
	
	public static int taskID;
	public static boolean flag = true;
	public static boolean DMcountdown = false;
	public static String winningMap;
	public static double DMDistance;
	private double xCenter;
	private double yCenter;
	private double zCenter;
	
	public void startSG() {
		
	        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        taskID = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
	            @Override
	            public void run() {
	            	
	            	//dummy to make sure no database timeout
	            	
	            	if (count == 0) {
	            		if (Main.connected == true) {
	            			data.dummy();
	            		}
	            	}

	            	count++;
	            	if (count == 60) {
	            		count = 0;
	            		if (Main.connected == true) {
	            			data.dummy();
	            		}
	            		
	            		if (Main.gameState.equals(GameState.INGAME)) {
	            			Bukkit.broadcastMessage(prefix + StringFunctions.surround(String.valueOf(PlayersSpecs.players.size()), ChatColor.RED + "") + ChatColor.GREEN + " tribute(s) still remain.");
	            		}
	            	}
	            	
	            	//Updating scoreboard
					for (Player p: Bukkit.getServer().getOnlinePlayers()) {
						if (gameState.equals(GameState.LOBBY)) {
							SGScoreboards.updateLobbyScoreboard(p);	
						} else if (gameState.equals(GameState.PREGAME))	{
							SGScoreboards.updatePreScoreboard(p);
						} else if (gameState.equals(GameState.INGAME)) {
							SGScoreboards.updateInGameScoreboard(p);
						} else if (gameState.equals(GameState.PREDM)) {
							SGScoreboards.updatePreDMScoreboard(p);
						} else if (gameState.equals(GameState.DEATHMATCH)) {
							SGScoreboards.updateDMScoreboard(p);
						} else if (gameState.equals(GameState.CLEANUP)) {
							SGScoreboards.updateCleanupScoreboard(p);
						}
								
					} 		
					
					//LOBBY
					if (gameState.equals(GameState.LOBBY)) {
						if (Bukkit.getOnlinePlayers().size() >= minPlayers && flag == true) {
							
							lobbyTimeLeft -= 1;
							
							if (lobbyTimeLeft == -1) {
								
								
								//Declare map winner, teleport players to map.
								winningMap = Transition.determineWinner();
								ChestOpenEvent.getTier2Locations(winningMap); //winningMap.toLowerCase();
								
								Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + winningMap + ChatColor.GREEN +  " will be played!");
								flag = false;
								Transition.teleportPlayers(winningMap);
								Bukkit.getWorld(winningMap).setTime(0);
								
								
								//distance for lightning strikes in DM
								DMDistance = 1.3*Transition.DMLightningDist(winningMap);
								
								gameState = GameState.PREGAME;
								
								
								//new scoreboard
								for (Player p: Bukkit.getServer().getOnlinePlayers()) {
									p.getScoreboard().getObjective("Lobby").getScore(ChatColor.WHITE + "Watching: ").setScore(2);
									SGScoreboards.updatePreScoreboard(p);
								}
								
				
							}
							
							if (lobbyTimeLeft <= 9 && lobbyTimeLeft >= 0) {
								Bukkit.broadcastMessage(prefix + ChatColor.RED + (lobbyTimeLeft+1) + ChatColor.GREEN + " seconds left until voting ends!");
							}
						}
						
						if (Bukkit.getOnlinePlayers().size() < minPlayers) {
							flag = true;
							lobbyTimeLeft = lobbyMinTime;						
						}
					}
					
					//PREGAME
					if (gameState.equals(GameState.PREGAME))	{ 
						
						preTimeLeft -= 1;
						
						if (preTimeLeft <= 9 && preTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.RED + (preTimeLeft+1) + ChatColor.GREEN + " seconds left until the game starts!");
						}
						
						
						if (preTimeLeft == -1) {
							
							
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Let the Games Begin!");
							gameState = GameState.INGAME;
							for (Player p: Bukkit.getOnlinePlayers()) {
								SGScoreboards.updateInGameScoreboard(p);
							}
						}
						
						//did someone win?
						if (PlayersSpecs.players.size() == 1) {
							Celebration.celebrate();				
						} else if (PlayersSpecs.players.size() == 0) {
							gameState = GameState.CLEANUP;
						}
						
						
						
					}
					
					//INGAME	
					
					if (gameState.equals(GameState.INGAME)) {
						
						inGameTimeLeft -= 1;
						
						if (PlayersSpecs.players.size() <= 3 && DMcountdown == false) {
							DMcountdown = true;
							if (inGameTimeLeft > 60) {
								inGameTimeLeft = 60;
								for (Player p: Bukkit.getOnlinePlayers()) {
									SGScoreboards.updateInGameScoreboard(p);
								}
							}
							
						}
						
						//chest refill
						if (inGameTimeLeft == refill1Time-1) {
							ChestOpenEvent.refill1 = true;
							Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + "Sponsors have refilled the chests!");	
						} else if (inGameTimeLeft == refill2Time-1) {
							ChestOpenEvent.refill2 = true;
							Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + "Sponsors have refilled the chests!");	
						}
						
						if (inGameTimeLeft == 59) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Players will be teleported to Deathmatch in 60 seconds!");						
						} else if (inGameTimeLeft == 29) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Players will be teleported to Deathmatch in 30 seconds!");
						} else if (inGameTimeLeft <= 9 && inGameTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Players will be teleported to Deathmatch in " + ChatColor.RED + (inGameTimeLeft+1) + ChatColor.GREEN + " seconds!");
						} else if (inGameTimeLeft == -1) {
							
							Transition.teleportDM(winningMap);
							gameState = GameState.PREDM;
							
							xCenter = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + winningMap + ".pos" + 0 + ".x"); //toLowerCase()
							yCenter = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + winningMap + ".pos" + 0 + ".y");
							zCenter = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + winningMap + ".pos" + 0 + ".z");
							

						
							for (Player p: Bukkit.getOnlinePlayers()) {
								SGScoreboards.updatePreDMScoreboard(p);
							}
						}
						
						//did someone win?
						if (PlayersSpecs.players.size() == 1) {
							Celebration.celebrate();
						} else if (PlayersSpecs.players.size() == 0) {
							gameState = GameState.CLEANUP;
						}
					}
					
					//PREDM
					if (gameState.equals(GameState.PREDM)) {
						preDMTimeLeft -= 1;
						
						if (preDMTimeLeft <= 9 && preDMTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will begin in " + ChatColor.RED + (preDMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
						} else if (preDMTimeLeft == -1) {
							gameState = GameState.DEATHMATCH;
						}
						
						//did someone win?
						
						if (PlayersSpecs.players.size() == 1) {
							Celebration.celebrate();
						} else if (PlayersSpecs.players.size() == 0) {
							gameState = GameState.CLEANUP;
						}
					}
					
					//DM
					if (gameState.equals(GameState.DEATHMATCH)) {
						DMTimeLeft -= 1;
						
						//get lightning dist
						for (Player p: PlayersSpecs.players) {
							if (DMTimeLeft % 3 == 0) {
								Location loc = p.getLocation();
								int x = loc.getBlockX();
								//int y = loc.getBlockY();
								int z = loc.getBlockZ();
								
								if (Math.sqrt((x-xCenter)*(x-xCenter) + (z-zCenter)*(z-zCenter)) > DMDistance) {
									p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.LIGHTNING);
									p.sendMessage(prefix + ChatColor.RED + "Please return to spawn!"); 
									
								}
								
								
							}
							
						}
						
						if (DMTimeLeft == 119) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will end in " + ChatColor.RED + 2 + ChatColor.GREEN +  " minutes!");
						} else if (DMTimeLeft == 59) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will end in " + ChatColor.RED + (DMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
							DMDistance /= 2;
						} else if (DMTimeLeft == 29) {
							DMDistance /= 2;
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will end in " + ChatColor.RED + (DMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
						} else if (DMTimeLeft <= 9 && DMTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will begin in " + ChatColor.RED + (DMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
							DMDistance /= 1.2;
						} else if (DMTimeLeft == -1) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "The game has ended!");
							gameState = GameState.CLEANUP;
						}
						
						//did someone win?
						if (PlayersSpecs.players.size() == 1) {

							
							Celebration.celebrate();
						} else if (PlayersSpecs.players.size() == 0) {
							
							gameState = GameState.CLEANUP;
						}
					}
					
					//CLEANUP
					if (gameState.equals(GameState.CLEANUP)) {
						cleanupTimeLeft -= 1;
						
						if (cleanupTimeLeft == -1) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "The server is restarting.");
							
							for (Player p: Bukkit.getOnlinePlayers()) {
								//p.kickPlayer("Server is restarting");
								bungee.connect(p, "hub");
							}
							
						}
						
						if (cleanupTimeLeft == -4) {
							
							//update stats at once at the end of the game
							if (Main.connected == true) {
								for (Player p: PlayersSpecs.playersKills.keySet()) {
									UUID uuid = p.getUniqueId();

									
									data.setStat(uuid, "XP", data.getStat(uuid, "XP") + PlayersSpecs.playersXP.get(p));
									data.setStat(uuid, "KILLS", data.getStat(uuid, "KILLS") + PlayersSpecs.playersKills.get(p));
									data.setStat(uuid, "CHESTS", data.getStat(uuid, "CHESTS") + PlayersSpecs.playersChests.get(p));
									
									Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "\nPLAYER STATS UPDATED!\n");
									
									int newLevel = SQLevents.getProgress(p)[0];
									
									data.setStat(uuid, "LEVEL", newLevel);
									
									
																		
									//data.setStat(uuid, "KDR", kdr);
									
								}
							}
							
							Bukkit.shutdown();	
						}
					}
	            }
	        }, 0L, 20L);
	    }
	
	public void updateStatus() {
		new BukkitRunnable() {
			@Override
			public void run() {
				statusdata.setStat(serverName, "STATUS", "");
				statusdata.setStat(serverName, "PLAYERS", PlayersSpecs.players.size());
			}	
		}.runTaskTimerAsynchronously(this, 20L, 60L);
	}
	
	

    //Unloading maps, to rollback maps. Will delete all player builds until last server save
    public static void unloadMap(String mapname){
        if(Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false)){
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "SUCCESSFULLY UNLOADED " + mapname);
        }else{
        	Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "COULD NOT UNLOAD " + mapname);
        }
    }
    
    //Loading maps (MUST BE CALLED AFTER UNLOAD MAPS TO FINISH THE ROLLBACK PROCESS)
    public static void loadMap(String mapname){
        Bukkit.getServer().createWorld(new WorldCreator(mapname));
    }
 
    //Maprollback method, because were too lazy to type 2 lines
    public static void rollback(String mapname){
        unloadMap(mapname);
        loadMap(mapname);
    }
	
	
	
    
	


}