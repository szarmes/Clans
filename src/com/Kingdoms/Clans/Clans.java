package com.Kingdoms.Clans;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

public class Clans {

	//Clans Data
	public static HashMap<String, TeamPlayer> Users = new HashMap<String, TeamPlayer>();
	public static HashMap<String, Team> Teams = new HashMap<String, Team>(); 
	public static HashMap<String, TeamArea> TeamAreas = new HashMap<String, TeamArea>();
	
	//Files
	File TeamsFile;
	File PlayersFile;
	
	//Logger
	Logger log = Logger.getLogger("Minecraft");//Define your logger
	
	
	public void onEnable() {
		
		//Team File
		TeamsFile = new File("plugins/Clans/Teams.yml");
		//Players File
		TeamsFile = new File("plugins/Clans/Players.yml");
		//Load Data From Files
		loadData();
	
	}
	public void onDisable() {
		log.info("Clans disabled.");
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		String commandName = cmd.getName().toLowerCase();
        if (sender instanceof Player) 
        {
            Player player = (Player) sender;
            TeamPlayer tPlayer = ClansUsers.get(player.getDisplayName());
            
            if(commandName.equals("team") && args.length >= 1)
            {
            	switch( args[0].toUpperCase() )
            	{
            		case "CREATE": break;
            		case "INVITE": break;
            		case "ACCEPT": break;
            		case "REJECT": break;
            		case "LIST": break;
            		case "INFO": break;
            		case "ONLINE": break;
            		case "LEAVE": break;
            		case "TK": break;
            		case "TOPSCORELIST": break;
            		case "SCORE": break;
            		case "KICK": break;
            		case "RCREATE": break;
            		case "RSET": break;
            		case "RRENAME": break;
            		case "RMASSMOVE": break;
            		case "RINFO": break;
            		case "RPERMISSION": break;
            		case "RDELETE": break;
            		case "DISBAND": break;
            		case "TAG": break;
            		case "COLOR": break;
            		case "MOTD": break;
            		case "HELP": break;
            		case "AREA": 
            			break;
            	}
            }
            else if(commandName.equals("t"))
            {
            	
            }
            else if(commandName.equals("elo"))
            {
            	if(args[0].toUpperCase() == "LIST")
            	{
            		
            	}
            	else
            	{
            		//Must be a player name
            	}
            }
            
		
		
        }
        return true;
	}
	private void loadData()
	{
		//Load Clans and Players from Files.
		
		//Load Players First
		
		//Load Clans Second
	}
	private void saveAllData()
	{
		//Print Clans and Players to Files.
	}
	private void savePlayersData()
	{
		//Print Clans and Players to Files.
	}
	
	
	
	
	
	
	
}
