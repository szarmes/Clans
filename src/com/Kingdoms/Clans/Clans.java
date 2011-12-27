package com.Kingdoms.Clans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;



public class Clans extends JavaPlugin {

	//Clans Data
	public static HashMap<String, TeamPlayer> Users = new HashMap<String, TeamPlayer>();
	public static HashMap<String, Team> Teams = new HashMap<String, Team>(); 
	public static HashMap<String, TeamArea> TeamAreas = new HashMap<String, TeamArea>();

	//Files
	private File TeamsFile;
	private File PlayersFile;

	//Logger
	private Logger log = Logger.getLogger("Minecraft");//Define your logger
	
	//Listeners
	private final ClansPlayerListener playerListener = new ClansPlayerListener(this);
	
	


	public void onEnable() {
		
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        
		//Team File
		TeamsFile = new File("plugins/Clans/Teams.yml");
		//Players File
		PlayersFile = new File("plugins/Clans/Players.yml");
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
            String PlayerName = player.getDisplayName();
            TeamPlayer tPlayer = Users.get(PlayerName);
            
            if(commandName.equals("team") && args.length >= 1)
            {
            	switch(args[0].toUpperCase() )
            	{
            		/* ==============================================================================
            		 *	TEAM CREATE - Creates a team.
            		 * ============================================================================== */
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
            				Users.get(PlayerName).setTeamKey(TeamName);
            				//Create New Team and Add to Teams
            				Teams.put(TeamName, new Team(PlayerName));
            				player.sendMessage(ChatColor.GREEN + "Team " + TeamName +" successfully created!");
            			}
            			break;
            		/* ==============================================================================
                     *	TEAM INVITE - Invites a player to the team
                     * ============================================================================== */   
            		case "INVITE": 
            			if(args.length < 2){ //NOT ENOUGH ARGS
            				player.sendMessage(ChatColor.RED + "You didn't invite anyone.");
            				return true;
            			}
            			else if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "Must have a team to be able to invite to one.");
            				return true;
            			}
            			else if (!getRank(PlayerName).canInvite()) { //NOT ALLOWED TO INVITE
            				player.sendMessage(ChatColor.RED + "You lack sufficient permissions to invite on this team.");
            				return true;
            			}
            			
            			else if(!Users.containsKey(args[1])){ // INVITED NAME DOESN'T EXIST
            				player.sendMessage(ChatColor.RED + "That player does not exist.");
            				return true;
            			}
            			else{
            				TeamPlayer invitedPlayer = Users.get(args[1]);
            				if(invitedPlayer.hasTeam()){ // INVITED PLAYER HAS A TEAM
            					player.sendMessage(ChatColor.RED + "Cannot invite: This player has a team already.");
            					return true;
            				}
            				else{ // GIVE INVITE TO INVITED PLAYER
            					Users.get(args[1]).setInvite(tPlayer.getTeamKey());
            				}
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM ACCEPT - Accepts an invite
                	 * ============================================================================== */           		
            		case "ACCEPT": 
            			if(args.length != 1){ // TOO MANY ARGUMENTS
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Proper use is /team accept");
            				return true;
            			}           			
            			else if(tPlayer.hasTeam()){ // PLAYER HAS A TEAM
            				player.sendMessage(ChatColor.RED + "You are already on a team.");
            				return true;
            			}
            			else if(tPlayer.getInvite() == ""){ //PLAYER HAS NO INVITATIONS
            				player.sendMessage(ChatColor.RED + "You have not been invited to a team.");
            				return true;
            			}
            			
            			else {
            				teamAdd(PlayerName);
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM REJECT - Rejects an invite
                	 * ============================================================================== */            		
            		case "REJECT": 
            			//we will check to see if they have an invite, if they do, we will clear all invites and send message
            			if(args.length != 1){     
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Proper use is /team reject");
            				return true;
             			}
            			else if(tPlayer.getInvite() == ""){
        					player.sendMessage(ChatColor.RED + "You do not have an invite to reject.");
        					return true;
        				}
        				else{
        					player.sendMessage(ChatColor.RED + "You have rejected the offer from '" + tPlayer.getInvite() + "'.");
        					Users.get(PlayerName).clearInvite();
        				}        				
            			break;
                	/* ==============================================================================
                	 *	TEAM LIST - Lists all teams
                	 * ============================================================================== */
            		case "LIST": 
            			if(args.length != 1){
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Proper use is /team list");
            			}
            			else{
            				for(String key : Teams.keySet()){
            					Team team = Teams.get(key);
            					player.sendMessage(team.getColor() + "[" + team.getTeamTag() + "] " 
            							+ ChatColor.GRAY + key + "( )"); //add team size later
            		        }
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM INFO - Prints info about a team
                	 * ============================================================================== */
            		case "INFO": 
            			if(args.length == 1) {
            				//check to see if they have a team
            				 if(!tPlayer.hasTeam()){
            					 player.sendMessage(ChatColor.RED + "You are not in a team. Use /team info <TEAMNAME> to look up a team's info.");
            					 return true;
            				 }
            				 else {
            					 Team team = Teams.get(tPlayer.getTeamKey());
            					 player.sendMessage(team.getColor() + "[" + tPlayer.getTeamKey() + "]" + " Team Info" );
            					 player.sendMessage(team.getTeamInfo());
            				 }	 
            			}
            			else {
            				//check to see if other teams exist
            				int i;
            				String TeamName = args[1];
            				for(i=2;i<args.length;i++)
            					TeamName += " " + args[i];
            				Team team = Teams.get(TeamName);
            				player.sendMessage(team.getColor() + "[" + TeamName + "]" + " Team Info" );
            				player.sendMessage(team.getTeamInfo());
            			}
            			break;
            		/* ==============================================================================
                     *	TEAM ONLINE - Prints players in team that are online
                     * ============================================================================== */   
            		case "ONLINE": break;
                	/* ==============================================================================
                	 *	TEAM LEAVE - Leave a team
                	 * ============================================================================== */
            		case "LEAVE": 
            			if(args.length != 1){ // TOO MANY ARGUMENTS
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Proper use is /team leave");
            			}           			
            			else if(!tPlayer.hasTeam()){ // PLAYER DOES NOT HAVE A TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team");
            			}
            			else{
            				Team team = Teams.get(tPlayer.getTeamKey());
            				if(team.isLeader(PlayerName)){	
            					if(team.getLeaderCount() == 1){
            						player.sendMessage(ChatColor.RED + "Must promote someone else to leader before leaving.");
            					}
            					else
            						teamRemove(PlayerName);
            				}
            				else
            					teamRemove(PlayerName);
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM TK - Toggles friendly fire
                	 * ============================================================================== */
            		case "TK": break;
                	/* ==============================================================================
                	 *	TEAM TOPSCORELIST - Prints the top 5 teams based on score
                	 * ============================================================================== */
            		case "TOPSCORELIST": break;
                	/* ==============================================================================
                	 *	TEAM SCORE - Prints the score of the team
                	 * ============================================================================== */
            		case "SCORE": break;
                	/* ==============================================================================
                	 *	TEAM KICK - Kicks a player from a team
                	 * ============================================================================== */
            		case "KICK": 
            			if(args.length < 2){ //NOT ENOUGH ARGS
            				player.sendMessage(ChatColor.RED + "You didn't kick anyone");
            				return true;
            			}
            			else if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "Must have a team to be able to use that command");
            				return true;
            			}
            			else if (!getRank(PlayerName).canKick()) { //NOT ALLOWED TO KICK
            				player.sendMessage(ChatColor.RED + "You lack sufficient permissions to kick on this team");
            				return true;
            			}
            			
            			else if(!Users.containsKey(args[1])){ // KICKED NAME DOESN'T EXIST
            				player.sendMessage(ChatColor.RED + "That player does not exist");
            				return true;
            			}
            			else{
            				//kick out of team
            				teamRemove(args[1]);
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RCREATE | RANKCREATE - Creates a new rank at the bottom of the team
                	 * ============================================================================== */
            		case "RCREATE": case "RANKCREATE": break;
                	/* ==============================================================================
                	 *	TEAM RSET | RANKSET - Sets a player's rank
                	 * ============================================================================== */
            		case "RSET": case "RANKSET": break;
                	/* ==============================================================================
                	 *	TEAM RRENAME | RANKRENAME - Sets a rank's name
                	 * ============================================================================== */
            		case "RRENAME": case "RANKRENAME": break;
                	/* ==============================================================================
                	 *	TEAM RMASSMOVE | RANKMASSMOVE - Moves all players of a rank to another
                	 * ============================================================================== */
            		case "RMASSMOVE": case "RANKMASSMOVE": break;
                	/* ==============================================================================
                	 *	TEAM RINFO | RANKINFO - Prints permissions of a rank
                	 * ============================================================================== */
            		case "RINFO": case "RANKINFO": break;
                	/* ==============================================================================
                	 *	TEAM RPERMISSION | RANKPERMISSION - Sets a permission of a rank
                	 * ============================================================================== */
            		case "RPERMISSION": case "RANKPERMISSION": break;
                	/* ==============================================================================
                	 *	TEAM RDELETE | RANKDELETE - Removes a rank and moves all players inside to bottom rank
                	 * ============================================================================== */
            		case "RDELETE": case "RANKDELETE": break;
                	/* ==============================================================================
                	 *	TEAM DISBAND - Disbands the entire team
                	 * ============================================================================== */
            		case "DISBAND": 
            			break;
                	/* ==============================================================================
                	 *	TEAM TAG - Sets a team's tag
                	 * ============================================================================== */
            		case "TAG": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            			}
            			else{
            				Team team = Teams.get(tPlayer.getTeamKey());
            				if(!team.isLeader(PlayerName)){
            					player.sendMessage(ChatColor.RED + "Must be team leader to edit tag.");
            				}
            				else if(args.length == 1){
            					player.sendMessage(ChatColor.RED + "Your current tag is [" + team.getTeamTag() + "]. /team tag <edit> to change tag.");
            				}
            				else if(args.length > 2){
            					player.sendMessage(ChatColor.RED + "Tags must be one word.");
            				}
            				else if(args[1].length() > 7){
            					player.sendMessage(ChatColor.RED + "Tags must be less than seven characters.");
            				}
            				else{
            					Teams.get(tPlayer.getTeamKey()).setTeamTag(args[1]);
            					player.sendMessage(ChatColor.RED +"Tag has been changed.");
            				}
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM COLOR | COLOUR - Sets a team's color
                	 * ============================================================================== */
            		case "COLOR": case "COLOUR": 
            			
            			break;
                	/* ==============================================================================
                	 *	TEAM MOTD - Set's a team's Message of the Day, prints if no argument 
                	 * ============================================================================== */
            		case "MOTD": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            			}
            			else{
            				Team team = Teams.get(tPlayer.getTeamKey());
            				if(args.length == 1){ //DISPLAY MOTD
            					player.sendMessage(ChatColor.GREEN + team.getMOTD());
            				}
            				else if(!team.isLeader(PlayerName)){ //NOT TEAM LEADER
            					player.sendMessage(ChatColor.RED + "Must be the team leader to edit the MOTD.");
            				}
            				else{
            					String MOTD = args[1];
            					int i;
            					for(i=2;i<args.length;i++)
            						MOTD += " " + args[i];
            					Teams.get(tPlayer.getTeamKey()).setMOTD(MOTD);	
            				}
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM HELP - Prints commands and how to use them
                	 * ============================================================================== */
            		case "HELP": break;
                	/* ==============================================================================
                	 *	TEAM AREA - THIS ISNT SET UP CORRECTLY YET
                	 * ============================================================================== */
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
		/*
		 * LOAD PLAYERS FROM FILE
		 * 
		 */
		HashMap<String,HashMap<String,String>> pl = null;
		Yaml yamlPlayers = new Yaml();
		Reader reader = null;
        try {
            reader = new FileReader(PlayersFile);
        } catch (final FileNotFoundException fnfe) {
        	 System.out.println("Players.YML Not Found!");
        	   try{
	            	  String strManyDirectories="plugins/Clans";
	            	  boolean success = (new File(strManyDirectories)).mkdirs();
	            	  }catch (Exception e){//Catch exception if any
	            	  System.err.println("Error: " + e.getMessage());
	            	  }
        } finally {
            if (null != reader) {
                try {
                    pl = (HashMap<String,HashMap<String,String>>)yamlPlayers.load(reader);
                    reader.close();
                } catch (final IOException ioe) {
                    System.err.println("We got the following exception trying to clean up the reader: " + ioe);
                }
            }
        }
        if(pl != null)
        {
        	//TODO: Load Player data into Users
        	for(String key : pl.keySet())
        	{
        		HashMap<String,String> PlayerData = pl.get(key);
        		String[] sDate = PlayerData.get("LastOnline").split("/");
        		int month = Integer.parseInt(sDate[0]);
        		int day = Integer.parseInt(sDate[1]);
        		int year = Integer.parseInt(sDate[2]);
        		Calendar cal = Calendar.getInstance();
        		cal.set(year, month, day);
        		int elo = Integer.parseInt(PlayerData.get("ELO"));
        		Users.put(key, new TeamPlayer(elo, cal));
        	}
        }


		/*
		 * LOAD TEAMS FROM FILE
		 * 
		 */
		HashMap<String, HashMap<String,Object>> h = null;
		Yaml yaml = new Yaml();
        try {
            reader = new FileReader(TeamsFile);
            h = (HashMap<String, HashMap<String,Object>>)yaml.load(reader);
        } catch (final FileNotFoundException fnfe) {
        	 System.out.println("Teams.YML Not Found!");
        	   try{
	            	  String strManyDirectories="plugins/Clans";
	            	  boolean success = (new File(strManyDirectories)).mkdirs();
	            	  }catch (Exception e){//Catch exception if any
	            	  System.err.println("Error: " + e.getMessage());
	            	  }
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (final IOException ioe) {
                    System.err.println("We got the following exception trying to clean up the reader: " + ioe);
                }
            }
            
        }
       //CREATE TEAMS ONE AT A TIME
       if(h != null)
       {  
    	   //System.out.println(h.toString());
    	   for(String key : h.keySet())
    	   {
    		  ///Get Hashmap containing all Team Data
    		   HashMap<String,Object> t = h.get(key);
    		   
    		   String MOTD = (String) t.get("Motd");
    		   String Tag = (String) t.get("Tag");
    		   String Color = (String) t.get("Color");
    		   int Score = Integer.parseInt(((String) t.get("Score")));
    		   
    		   //Create Tier Lists
    		   ArrayList<TierList> TeamList = new ArrayList<TierList>();
    		   HashMap<String,HashMap<String,Object>> List = (HashMap<String, HashMap<String, Object>>) t.get("List");
    		   for(String rankNumber : List.keySet())
    		   {
    			   HashMap<String,Object> Tier = List.get(rankNumber);
    			   //Create Rank
    			   TeamRank newRank = new TeamRank((String)Tier.get("Rank Name"),(HashMap<String,Boolean>)Tier.get("Permissions"));
    			   
    			   //Add TeamKeys to all Members
    			   for(String PlayerName : (HashSet<String>)Tier.get("Members"))
    				   //Users.get(PlayerName).setTeamKey(key);
    			   
    			   //Add Tier to TeamList
    			   TeamList.add(new TierList(newRank, (HashSet<String>)Tier.get("Members")));
    		   }
    		   //Add to Teams
    		   //Teams.put(key, new Team(TeamList, MOTD, Score, Tag, Color));
    		   
    		   //TODO: Add Team Area Info
    	   }
       }
	}
	private void saveTeams()
	{
		//Print Clans and Players to Files.
	}
	private void savePlayers()
	{
		//Print Clans and Players to Files.
	}
	private TeamRank getRank(String PlayerName)
	{
		TeamPlayer tPlayer = Users.get(PlayerName);
		return Teams.get(tPlayer.getTeamKey()).getRank(PlayerName);
	}
	private void teamAdd(String PlayerName){
		TeamPlayer tPlayer = Users.get(PlayerName);
		Users.get(PlayerName).setTeamKey(tPlayer.getInvite());
		Teams.get(tPlayer.getTeamKey()).addMember(PlayerName);
		Users.get(PlayerName).clearInvite();
	}
	private void teamRemove(String PlayerName){
		TeamPlayer tPlayer = Users.get(PlayerName);
		Teams.get(tPlayer.getTeamKey()).removeMember(PlayerName);
		Users.get(PlayerName).clearTeamKey();
	}
	public boolean hasUser(String PlayerName)
	{
		return Users.containsKey(PlayerName);
	}
	public void makeUser(String PlayerName)
	{
		Users.put(PlayerName, new TeamPlayer());
	}
	public void updateUserDate(String PlayerName)
	{
		Users.get(PlayerName).updateLastSeen();
	}



}