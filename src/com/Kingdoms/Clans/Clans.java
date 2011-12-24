package com.Kingdoms.Clans;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
            TeamPlayer tPlayer = Users.get(player.getDisplayName());
            
            if(commandName.equals("team") && args.length >= 1)
            {
            	switch(args[0].toUpperCase() )
            	{
            		case "CREATE": 
            			if(args.length < 2) {
            				player.sendMessage(ChatColor.RED + "Invalid number of arguments.");
            				return true;
            			}
            			else if(tPlayer.hasTeam()) {
            				player.sendMessage(ChatColor.RED + "You are already in a team.");
            				return true;
            			}
            			else{
            				int i;
            				String TeamName = args[1];
            				for(i=2;i<args.length;i++)
            					TeamName += " " + args[i];
            				//Set Player's Team to new Key
            				Users.get(player.getDisplayName()).setTeamKey(TeamName);
            				//Create New Team and Add to Teams
            				Teams.put(TeamName, new Team(player.getDisplayName()));
            				player.sendMessage(ChatColor.GREEN + "Team " + TeamName +" successfully created!");
            			}
            			break;
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
            		case "RCREATE": case "RANKCREATE": break;
            		case "RSET": case "RANKSET": break;
            		case "RRENAME": case "RANKRENAME": break;
            		case "RMASSMOVE": case "RANKMASSMOVE": break;
            		case "RINFO": case "RANKINFO": break;
            		case "RPERMISSION": case "RANKPERMISSION": break;
            		case "RDELETE": case "RANKDELETE": break;
            		case "DISBAND": break;
            		case "TAG": break;
            		case "COLOR": case "COLOUR": break;
            		case "MOTD": break;
            		case "HELP": break;
            		case "AREA": 
            			break;           			
            	}
        		return true;
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
