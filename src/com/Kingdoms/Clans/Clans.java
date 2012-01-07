package com.Kingdoms.Clans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
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
        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
        
		//Team File
		TeamsFile = new File("plugins/Clans/Teams.yml");
		//Players File
		PlayersFile = new File("plugins/Clans/Players.yml");
		//Load Data From Files
		loadData();
		
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
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
            	switch(args[0].toUpperCase())
            	{
            		/* ==============================================================================
            		 *	TEAM CREATE - Creates a team.
            		 * ============================================================================== */
            		case "CREATE": 
            			if(args.length < 2) {//INVALID ARGUMENTS
            				player.sendMessage(ChatColor.RED + "Invalid number of arguments.");
            				return true;
            			}
            			else if(tPlayer.hasTeam()) {//PLAYER HAS TEAM
            				player.sendMessage(ChatColor.RED + "You are already in a team.");
            				return true;
            			}
            			else if(args[1].length() > 30 ){//MORE THAN 30 CHARACTERS
            				player.sendMessage(ChatColor.RED + "Team names must be less than 30 characters.");
            				return true;
            			}
            			else{ //CREATE TEAM
            				int i;
            				String TeamName = args[1];
            				for(i=2;i<args.length;i++)
            					TeamName += " " + args[i];
            				if(Teams.containsKey(TeamName)) {
            					player.sendMessage(ChatColor.RED + "A team with this name already exists, please choose another team name.");
            					return true;
            				}
            				//Set Player's Team to new Key
            				Users.get(PlayerName).setTeamKey(TeamName);
            				//Create New Team and Add to Teams
            				Teams.put(TeamName, new Team(PlayerName));
            				player.sendMessage(ChatColor.GREEN + "Team [" + TeamName +"] successfully created!");
            				player.sendMessage(ChatColor.GREEN + "Use /team tag <tag> to add a Team tag.");
            				
            				saveTeams();
            			}
            			break;
            		/* ==============================================================================
                     *	TEAM INVITE - Invites a player to the team
                     * ============================================================================== */   
            		case "INVITE": 
            			if(args.length != 2){ //NOT ENOUGH ARGS
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
            					player.sendMessage(ChatColor.GREEN + "You have invited " + args[1] + " to your team.");
            					getServer().getPlayer(args[1]).sendMessage(ChatColor.GREEN + "You have been invited to " + tPlayer.getTeamKey() +". Type /team accept to or /team reject to accept or deny this offer.");
            				}
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM ACCEPT - Accepts an invite
                	 * ============================================================================== */           		
            		case "ACCEPT": 		
            			if(tPlayer.hasTeam()){ // PLAYER HAS A TEAM
            				player.sendMessage(ChatColor.RED + "You are already on a team.");
            				return true;
            			}
            			else if(tPlayer.getInvite() == ""){ //PLAYER HAS NO INVITATIONS
            				player.sendMessage(ChatColor.RED + "You have not been invited to a team.");
            				return true;
            			}
            			else { //ACCEPT INVITATION
            				player.sendMessage(ChatColor.GREEN + "You have accepted the invitation from " + tPlayer.getInvite() + ".");
            				teamAdd(PlayerName);
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM REJECT - Rejects an invite
                	 * ============================================================================== */            		
            		case "REJECT": 
            			if(!tPlayer.hasInvite()){
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
            			if(args.length != 1){//INVALID ARGUMENTS
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Proper use is /team list");
            			}
            			else{//GET TEAM LIST
            				for(String key : Teams.keySet()){
            					Team team = Teams.get(key);
            					player.sendMessage(team.getColor() + "[" + team.getTeamTag() + "] " 
            							+ ChatColor.GRAY + key + " ("+ team.getTeamSize() +")"); //add team size later
            		        }
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM INFO - Prints info about a team
                	 * ============================================================================== */
            		case "INFO": 
            			if(args.length == 1){//DISPLAY YOUR TEAM INFO
            				 if(!tPlayer.hasTeam()){//DOESNT HAVE TEAM
            					 player.sendMessage(ChatColor.RED + "You are not in a team. Use /team info <TEAMNAME> to look up a team's info.");
            					 return true;
            				 }
            				 else {//DISPLAY INFO
            					 Team team = Teams.get(tPlayer.getTeamKey());
            					 player.sendMessage(team.getColor() + "[" + tPlayer.getTeamKey() + "]" + " Team Info" );
            					 ArrayList<String> teamInfo = team.getTeamInfo();
            					 for(String s : teamInfo)
            						 player.sendMessage(s);
            				 }	 
            			}
            			else {//DISPLAY OTHER TEAM INFO
            				int i;
            				String TeamName = args[1];
            				for (i=2;i<args.length;i++)
            					TeamName += " " + args[i];
            				if(!Teams.containsKey(TeamName)) {//NAME DOESNT EXIST
            					player.sendMessage(ChatColor.RED + "Team '"+TeamName+"' does not exist.");
            					return true;
            				}
            				else {
            					Team team = Teams.get(TeamName);
            					player.sendMessage(team.getColor() + "[" + TeamName + "]" + " Team Info" );
           					 	ArrayList<String> teamInfo = team.getTeamInfo();
           					 	for(String s : teamInfo)
           					 		player.sendMessage(s);
            				}
            			}
            			break;
            		/* ==============================================================================
                     *	TEAM ONLINE - Prints players in team that are online
                     * ============================================================================== */   
            		case "ONLINE": 
            			 if(!tPlayer.hasTeam()) {//NOT ON A TEAM
             				player.sendMessage(ChatColor.RED + "You are not on a team.");
             				return true;
            			 }
             			 else {//CHECK ONLINE TEAM MEMBERS
             				 String teamKey = tPlayer.getTeamKey();
             				 Team team = Teams.get(tPlayer.getTeamKey());
             				 Player[] onlineList = getServer().getOnlinePlayers();
             				 
             				 int count = 0;
             				 String onlineMembers ="";
             				 
             				 for (Player p : onlineList)
             				 {
             					 String userTeamKey = Users.get(p.getDisplayName()).getTeamKey();
             					 if(userTeamKey.equals(teamKey))
             					 {
             						 count++;
             						 onlineMembers += p.getDisplayName() + ", ";
             					 }
             				 }
             				onlineMembers = onlineMembers.substring(0,onlineMembers.length()-2);
             				player.sendMessage(team.getColor() + "[" + teamKey + "] (" + count +"/"+ team.getTeamSize() + ") Online: ");
             				player.sendMessage(ChatColor.GRAY + onlineMembers);             				 
             			 }
            			break;
                	/* ==============================================================================
                	 *	TEAM LEAVE - Leave a team
                	 * ============================================================================== */
            		case "LEAVE":         			
            			if(!tPlayer.hasTeam()){ // PLAYER DOES NOT HAVE A TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team");
            				return true;
            			}
            			else if(getTeam(PlayerName).isLeader(PlayerName) && getTeam(PlayerName).getLeaderCount() == 1){//CANT LEAVE AS LEADER	
            				player.sendMessage(ChatColor.RED + "Must promote someone else to leader before leaving. Do /team disband if you are trying to disband the team.");
            				return true;
            			}
            			else {//LEAVE TEAM
            				player.sendMessage(ChatColor.GREEN + "You have left the team.");
            				teamRemove(PlayerName);		
            				
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM TK - Toggles friendly fire
                	 * ============================================================================== */
            		case "TK": 
            			if(args.length != 2) {//INVALID ARGUMENTS
            				player.sendMessage(ChatColor.RED + "Invalid number of arguments.");
            				return true;
            			}
            			else if(!tPlayer.hasTeam()){ // PLAYER DOES NOT HAVE A TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team");
            				return true;
            			}
            			else if (!(args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off"))) {//INVALID USE
            				player.sendMessage(ChatColor.RED + "Invalid use. Proper usage is /team tk <on/off>.");
            				return true;
            			}
            			else {//TOGGLE SETTING
            				Users.get(PlayerName).setCanTeamKill(args[1].equalsIgnoreCase("on"));
            				player.sendMessage(ChatColor.GREEN + "Team killing has been set to " + args[1] + ".");
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM TOPSCORELIST - Prints the top 5 teams based on score
                	 * ============================================================================== */
            		case "TOPSCORES": case "TOP": case "SB": break;
                	/* ==============================================================================
                	 *	TEAM SCORE - Prints the score of the team
                	 * ============================================================================== */
            		case "SCORE": break;
                	/* ==============================================================================
                	 *	TEAM KICK - Kicks a player from a team
                	 * ============================================================================== */
            		case "KICK": 
            			if(args.length == 1){ //NOT ENOUGH ARGS
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
            			if(getTeam(PlayerName).getRankNumber(PlayerName) >= getTeam(PlayerName).getRankNumber((args[1]))){//CANT ALTER LEADERS
        					player.sendMessage(ChatColor.RED + "Can not kick players with a higher rank than your own.");
            			}
            			else if(!Users.containsKey(args[1])){ // KICKED NAME DOESN'T EXIST
            				player.sendMessage(ChatColor.RED + "That player does not exist");
            				return true;
            			}
            			else{//KICK OUT OF TEAM
            				teamRemove(args[1]);
            				player.sendMessage(ChatColor.GREEN + "You have kicked " + args[1] + " out of the team.");
        					getServer().getPlayer(args[1]).sendMessage(ChatColor.RED + "You have been kicked out of the team.");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RCREATE | RANKCREATE - Creates a new rank at the bottom of the team
                	 * ============================================================================== */
            		case "RCREATE": case "RANKCREATE": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You must be in a team first.");
            				return true;
            			}
            			else if(!getRank(PlayerName).canEditRanks()){ //CANT EDIT RANKS
            				player.sendMessage(ChatColor.RED + "You lack sufficient permissions to create a rank on this team");
            				return true;
            			}
            			else if(args.length < 2){ //NO RANK ADDED
            				player.sendMessage(ChatColor.RED + "There is no rank to add.");
            				return true;
            			}
            			else if(args.length > 2){//MUST BE ONE WORD
            				player.sendMessage(ChatColor.RED + "Ranks must be one word");
            				return true;
            			}
            			else{ //ADD RANK
            				Teams.get(tPlayer.getTeamKey()).addRank(new TeamRank(args[1]));
            				player.sendMessage(ChatColor.GREEN + "You have added rank " + args[1] + " to the team.");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RSET | RANKSET - Sets a player's rank
                	 * ============================================================================== */
            		case "RSET": case "RANKSET": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You must be in a team first.");
            				return true;
            			}
            			else if(!getRank(PlayerName).canSetRanks()){ //CANT EDIT RANKS
            				player.sendMessage(ChatColor.RED + "You lack sufficient permissions to set ranks on this team");
            				return true;
            			}
            			else if(args.length != 3){ //NO RANK ADDED
            				player.sendMessage(ChatColor.RED + "Invalid use. Use /team rset <teammember> <ranknumber>.");
            				return true;
            			}
            			else if(!isInteger(args[2])){ //IF NO INTEGER
            				player.sendMessage(ChatColor.RED + "Invalid use. Use /team rset <teammember> <ranknumber>.");
            				return true;
            			}
            			else if(1 > Integer.parseInt(args[2])|| Integer.parseInt(args[2]) > getTeam(PlayerName).getRankCount()){//RANK NUMBER DOESNT EXIST
            				player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            				return true;	
            			}
            			else if(getTeam(PlayerName).isLeader(PlayerName) && getTeam(PlayerName).getLeaderCount() == 1 && args[1].equalsIgnoreCase(PlayerName)){//CANT DEMOTE SELF WITH NO LEADERS	
            				player.sendMessage(ChatColor.RED + "Must promote someone else to leader before changing your own rank.");
            				return true;
            			}
            			else if(!getTeam(PlayerName).isLeader(PlayerName)){//PLAYER ISNT LEADER
            				if(getTeam(PlayerName).getRankNumber(PlayerName) < Integer.parseInt(args[1])){//CANT ALTER LEADERS
            					player.sendMessage(ChatColor.RED + "Can not set rank of members higher than your own.");
            					return true;
            				}
            				else if(getTeam(PlayerName).getRankNumber(PlayerName) < Integer.parseInt(args[2])){//CANT SET LEADER AS A PLAYERS RANK
            					player.sendMessage(ChatColor.RED + "Can not set any members to a rank higher than your own.");
            					return true;
            				}
            				else{
            					Teams.get(tPlayer.getTeamKey()).changePlayerRank(args[1],Integer.parseInt(args[2]));
            					player.sendMessage(ChatColor.GREEN + "Rank Changed.");
            					saveTeams();
            				}
            			}
            			else{
        					Teams.get(tPlayer.getTeamKey()).changePlayerRank(args[1],Integer.parseInt(args[2]));
        					player.sendMessage(ChatColor.GREEN + "Rank Changed.");
        					saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RRENAME | RANKRENAME - Sets a rank's name
                	 * ============================================================================== */
            		case "RRENAME": case "RANKRENAME": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You must be in a team first.");
            				return true;
            			}
            			else if(!getRank(PlayerName).canEditRanks()){ //CANT EDIT RANKS
            				player.sendMessage(ChatColor.RED + "You lack sufficient permissions to rename ranks on this team");
            				return true;
            			}
            			else if(args.length != 3){
            				player.sendMessage(ChatColor.RED + "Invalid use. Use /team rrename <ranknumber> <newrankname>.");
            				return true;
            			}
            			else if(!isInteger(args[1])){
            				player.sendMessage(ChatColor.RED + "Rank Numbers must be digits.");
            				return true;
            			}
            			else if(1 > Integer.parseInt(args[1])|| Integer.parseInt(args[1]) > getTeam(PlayerName).getRankCount()){//RANK NUMBER DOESNT EXIST
            				player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            				return true;	
            			}
            			else if(getTeam(PlayerName).getRankNumber(PlayerName) < Integer.parseInt(args[1]) && !getTeam(PlayerName).isLeader(PlayerName)){
            				player.sendMessage(ChatColor.RED + "Cannot edit ranks higher than your own.");
            				return true;
            			}
            			else{
            				Teams.get(tPlayer.getTeamKey()).changeRankName(Integer.parseInt(args[1])-1,args[2]);
            				player.sendMessage(ChatColor.GREEN + "Rank name changed.");
            				
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RMASSMOVE | RANKMASSMOVE - Moves all players of a rank to another
                	 * ============================================================================== */
            		case "RMASSMOVE": case "RANKMASSMOVE": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You must be in a team first.");
            				return true;
            			}
            			else if(!getTeam(PlayerName).isLeader(PlayerName)){
            				player.sendMessage(ChatColor.RED + "Must be team leader to mass move people to different ranks.");
            				return true;
            			}
            			else if(args.length != 3){
            				player.sendMessage(ChatColor.RED + "Invalid use. Use /team rmassmove <oldranknumber> <newranknumber>.");
            				return true;
            			}
            			else if(!isInteger(args[1]) || !isInteger(args[2])){
            				player.sendMessage(ChatColor.RED + "Rank Numbers must be digits.");
            				return true;
            			}
            			else if(1 > Integer.parseInt(args[1])|| Integer.parseInt(args[1]) > getTeam(PlayerName).getRankCount()){//RANK NUMBER DOESNT EXIST
            				player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            				return true;	
            			}
            			else if(1 > Integer.parseInt(args[2])|| Integer.parseInt(args[2]) > getTeam(PlayerName).getRankCount()){//RANK NUMBER DOESNT EXIST
            				player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            				return true;	
            			}
            			else{
            				Teams.get(tPlayer.getTeamKey()).massRankMove(Integer.parseInt(args[1]),Integer.parseInt(args[2]));
            				player.sendMessage(ChatColor.GREEN + "Ranks moved.");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RINFO | RANKINFO - Prints permissions of a rank
                	 * ============================================================================== */
            		case "RINFO": case "RANKINFO": 
            			if(!tPlayer.hasTeam()){//NOT IN TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            				return true;
            			}
            			else if(args.length != 2){//INVALID ARGUMENTS
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Use /team rinfo <ranknumber> to get permissions.");
            				return true;
            			}
            			else if(!isInteger(args[1])){//MUST BE INTEGER
            				player.sendMessage(ChatColor.RED + "Rank Numbers must be digits.");
            				return true;
            			}
            			else if(1 > Integer.parseInt(args[1])|| Integer.parseInt(args[1]) > getTeam(PlayerName).getRankCount()){//RANK NUMBER DOESNT EXIST
            				player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            				return true;	
            			}
            			else{
            				Team team = Teams.get(tPlayer.getTeamKey());
            				if(team.getRankCount() < Integer.parseInt(args[1])){//RANK NUMBER DOESNT EXIST
            					player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            					return true;
            				}
            				else{//PRINT RANK INFO
            					
            					TeamRank rank = getTeam(PlayerName).getRank(Integer.parseInt(args[1]));
            					player.sendMessage(ChatColor.DARK_GREEN + rank.getRankName() + " Permissions:");
            					player.sendMessage(ChatColor.GREEN + "Set Ranks   : " + rank.canSetRanks());
            					player.sendMessage(ChatColor.GREEN + "Invite        : " + rank.canInvite());
            					player.sendMessage(ChatColor.GREEN + "Edit Ranks  : " + rank.canEditRanks());
            					player.sendMessage(ChatColor.GREEN + "Kick          : " + rank.canKick());
            					player.sendMessage(ChatColor.GREEN + "Team Chat  : " + rank.canTeamChat());
            					player.sendMessage(ChatColor.GREEN + "Area Info  : " + rank.canSeeAreaInfo());
            				}
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RPERMISSION | RANKPERMISSION - Sets a permission of a rank
                	 * ============================================================================== */
            		case "RPERMISSION": case "RANKPERMISSION": 
            			if(!tPlayer.hasTeam()){
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            				return true;
            			}
            			else if(!getRank(PlayerName).canEditRanks()){
            				player.sendMessage(ChatColor.RED + "You lack sufficent permission to edit rank permissions.");
            				return true;
            			}
            			else if(args.length != 4){
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Use /team rpermission <ranknumber> <kick/teamchat/rankedit/invite/promote> <true|false>.");
            				return true;
            			}
            			else if(!isInteger(args[1])){
            				player.sendMessage(ChatColor.RED + "Rank Numbers must be digits.");
            				return true;
            			}
            			else if(1 > Integer.parseInt(args[1])|| Integer.parseInt(args[1]) > getTeam(PlayerName).getRankCount()){//RANK NUMBER DOESNT EXIST
            				player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            				return true;	
            			}
            			else if(Teams.get(tPlayer.getTeamKey()).getRankNumber(PlayerName) < Integer.parseInt(args[1])){
            				player.sendMessage(ChatColor.RED + "Cannot edit ranks higher than your own.");
            			}
            			else{
            				switch(args[2].toUpperCase()){
            				case "KICK":
            					Teams.get(tPlayer.getTeamKey()).getRank(Integer.parseInt(args[1])).setCanKick(Boolean.parseBoolean(args[3]));
            					saveTeams();
            					break;
            				case "TEAMCHAT":
            					Teams.get(tPlayer.getTeamKey()).getRank(Integer.parseInt(args[1])).setCanTeamChat(Boolean.parseBoolean(args[3]));
            					saveTeams();
            					break;
            				case "RANKEDIT":
            					Teams.get(tPlayer.getTeamKey()).getRank(Integer.parseInt(args[1])).setCanEditRanks(Boolean.parseBoolean(args[3]));
            					saveTeams();
            					break;
            				case "INVITE":
            					Teams.get(tPlayer.getTeamKey()).getRank(Integer.parseInt(args[1])).setCanInvite(Boolean.parseBoolean(args[3]));
            					saveTeams();
            					break;
            				case "PROMOTE":
            					Teams.get(tPlayer.getTeamKey()).getRank(Integer.parseInt(args[1])).setCanSetRanks(Boolean.parseBoolean(args[3]));
            					saveTeams();
            					break;
            				default: 
            					player.sendMessage(ChatColor.RED + "Invalid permission. Use /team rpermission <ranknumber> <kick/teamchat/rankedit/invite/promote> <true|false>.");
            					return true;
            				}
            				player.sendMessage(ChatColor.GREEN + "Changed rank permission.");
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM RDELETE | RANKDELETE - Removes a rank and moves all players inside to bottom rank
                	 * ============================================================================== */
            		case "RDELETE": case "RANKDELETE": 
            			if(!tPlayer.hasTeam()){
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            				return true;
            			}
            			Team team = Teams.get(tPlayer.getTeamKey());
            			if(getTeam(PlayerName).getRankNumber(PlayerName) >= Integer.parseInt(args[1])){
            				player.sendMessage(ChatColor.RED + "Unable to remove ranks above your own or your own.");
            				return true;
            			}
            			else if(!getRank(PlayerName).canEditRanks()){
            				player.sendMessage(ChatColor.RED + "You lack sufficent permission to delete ranks.");
            				return true;
            			}
            			else if(!isInteger(args[1])){
            				player.sendMessage(ChatColor.RED + "Rank number must be in digits.");
            				return true;
            			}
            			else if(1 > Integer.parseInt(args[1])|| Integer.parseInt(args[1]) > getTeam(PlayerName).getRankCount()){//RANK NUMBER DOESNT EXIST
            				player.sendMessage(ChatColor.RED + "Rank number does not exist.");
            				return true;	
            			}
            			else if(team.getRankCount() < Integer.parseInt(args[2])){//RANK NUMBER DOESNT EXIST
        					player.sendMessage(ChatColor.RED + "Rank number does not exist.");
        					return true;
        				}
            			else{
            				
            				Teams.get(tPlayer.getTeamKey()).massRankMove(Integer.parseInt(args[1]),team.getRankCount()-1);
            				Teams.get(tPlayer.getTeamKey()).removeRank(Integer.parseInt(args[1]));
            				player.sendMessage(ChatColor.GREEN + "Ranks moved.");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM DISBAND - Disbands the entire team
                	 * ============================================================================== */
            		case "DISBAND": 
            			if(!tPlayer.hasTeam()){//NO TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            				return true;
            			}
            			else if (!getTeam(PlayerName).isLeader(PlayerName)) {//MUST BE LEADER
            				player.sendMessage(ChatColor.RED + "You must be the leader to disband the team.");
            				return true;
            			}
            			else {//DISBAND TEAM
            				String TeamKey = tPlayer.getTeamKey();
            				ArrayList<String> members = getTeam(PlayerName).getAllMembers();
            				for(String mem : members)
            					Users.get(mem).clearTeamKey();
            				Teams.remove(TeamKey);
            				
            				player.sendMessage(ChatColor.GREEN + "Your team has been succesfully disbanded.");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM TAG - Sets a team's tag
                	 * ============================================================================== */
            		case "TAG": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            				return true;
            			}
            			else if(!getTeam(PlayerName).isLeader(PlayerName)){
            				player.sendMessage(ChatColor.RED + "Must be team leader to edit tag.");
            				return true;
            			}
            			else if(args.length == 1){//PRINT CURRENT TAG
            				player.sendMessage(ChatColor.GREEN + "Your current tag is [" + getTeam(PlayerName).getTeamTag() + "]. /team tag <NewTag> to change tag.");
            				return true;
            			}
            			else if(args[1].length() < 2){//NOT ENOUGH CHARACTERS
            				player.sendMessage(ChatColor.RED + "Tags must be at least three characters.");
            				return true;
            			}
            			else if(args[1].length() > 7){//TOO MANY CHARACTERS
            				player.sendMessage(ChatColor.RED + "Tags must be less than seven characters.");
            				return true;
            			}
            			else {//CHANGE TAG
            				Teams.get(tPlayer.getTeamKey()).setTeamTag(args[1]);
            				player.sendMessage(ChatColor.GREEN +"Tag has been changed to [" + getTeam(PlayerName).getTeamTag() + "].");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM COLOR | COLOUR - Sets a team's color
                	 * ============================================================================== */
            		case "COLOR": case "COLOUR": 
            			if(!tPlayer.hasTeam()){
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            				return true;
            			}
            			else if(!getTeam(PlayerName).isLeader(PlayerName)){//ISNT LEADER
            				player.sendMessage(ChatColor.RED + "Must be the leader to change the team color.");
            				return true;
            			}
            			else if(args.length != 2){//INVALID ARGS
            				player.sendMessage(ChatColor.RED + "Invalid use of command. Use /team color <colorname>.");
            				return true;
            			}
            			else if(!Teams.get(tPlayer.getTeamKey()).validateColor(args[1])){ //INVALID COLOR
            				player.sendMessage(ChatColor.RED + "Invalid color. Choose from this list of colors: DARK_RED, RED, DARK_AQUA," +
            						"AQUA, DARK_GREEN, GREEN, DARK_BLUE, BLUE, DARK_PURPLE, PURPLE, GOLD, YELLOW, BLACK, GRAY");
            				return true;
            			}
            			else{//SET COLOR
            				Teams.get(tPlayer.getTeamKey()).setColor(args[1]);
            				player.sendMessage(ChatColor.GREEN + "Color changed.");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM MOTD - Set's a team's Message of the Day, prints if no argument 
                	 * ============================================================================== */
            		case "MOTD": 
            			if(!tPlayer.hasTeam()){ //NO TEAM
            				player.sendMessage(ChatColor.RED + "You are not in a team.");
            				return true;
            			}
            			else if(args.length == 1){ //DISPLAY MOTD WITH TEAM COLOR
            				ChatColor color = Teams.get(tPlayer.getTeamKey()).getColor();
            				player.sendMessage(color + "[Team MOTD] " + getTeam(PlayerName).getMOTD());
            				return true;
            			}
            			else if(!getTeam(PlayerName).isLeader(PlayerName)){ //NOT TEAM LEADER
            				player.sendMessage(ChatColor.RED + "Must be the team leader to edit the Message of the Day.");
            				return true;
            			}
            			else {
            				String MOTD = args[1];
            				int i;
            				for(i=2;i<args.length;i++)
            					MOTD += " " + args[i];
            				Teams.get(tPlayer.getTeamKey()).setMOTD(MOTD);	
            				player.sendMessage(ChatColor.GREEN + "Team MOTD has been changed.");
            				saveTeams();
            			}
            			break;
                	/* ==============================================================================
                	 *	TEAM HELP - Prints commands and how to use them
                	 * ============================================================================== */
            		case "HELP": 
            			if(args.length == 1){
                   			player.sendMessage(ChatColor.RED + "Use /team help [1-4] to view each page.");
                   			return true;
                   		}
            			else if(args[1].equalsIgnoreCase("1")) {
                   			player.sendMessage(ChatColor.RED + "General Team Commands:");
                   			player.sendMessage(ChatColor.RED + "/t <message>"+ChatColor.GRAY +" - Sends a message to your team.");
                   			player.sendMessage(ChatColor.RED + "/team create <teamname>"+ChatColor.GRAY +" - Creates a team.");
                   			player.sendMessage(ChatColor.RED + "/team invite <playername>"+ChatColor.GRAY +" - Invites a player to a team.");
                   			player.sendMessage(ChatColor.RED + "/team accept"+ChatColor.GRAY +" - Accepts recent team invite.");
                   			player.sendMessage(ChatColor.RED + "/team reject"+ChatColor.GRAY +" - Rejects recent team invite.");
                   			player.sendMessage(ChatColor.RED + "/team leave"+ChatColor.GRAY +" - Leave a team.");
                   			player.sendMessage(ChatColor.RED + "/team info"+ChatColor.GRAY +" - Lists players and rankings of your own team.");
                   		}
                   		else if(args[1].equalsIgnoreCase("2")) {
                   			player.sendMessage(ChatColor.RED + "General Team Commands Continued:");
                   			player.sendMessage(ChatColor.RED + "/team info <teamname>"+ChatColor.GRAY +" - Lists players and rankings of the specified team.");
                   			player.sendMessage(ChatColor.RED + "/team online"+ChatColor.GRAY +" - Lists online team members.");
                   			player.sendMessage(ChatColor.RED + "/team list"+ChatColor.GRAY +" - Lists all teams.");
                   			player.sendMessage(ChatColor.RED + "/team tag <teamtag>"+ChatColor.GRAY +" - Sets a team's tag.");
                   			player.sendMessage(ChatColor.RED + "/team color <color>"+ChatColor.GRAY +" - Sets a team's color.");
                   			player.sendMessage(ChatColor.RED + "/team motd |<message>"+ChatColor.GRAY +" - Displays or sets a team's message of the day.");
                   			player.sendMessage(ChatColor.RED + "/team kick <playername>"+ChatColor.GRAY +" - Kicks a player from the team.");
                   			player.sendMessage(ChatColor.RED + "/team tk <on/off>"+ChatColor.GRAY +" - Toggles friendly fire.");
                   		}
                   		else if(args[1].equalsIgnoreCase("3")) {
                   			player.sendMessage(ChatColor.RED + "Team Rank Commands:");
                   			player.sendMessage(ChatColor.RED + "/team rankcreate <rankname>"+ChatColor.GRAY +" - Creates new rank at the bottom of the rank structure.");
                   			player.sendMessage(ChatColor.RED + "/team rankrename <ranknumber> <rankname>"+ChatColor.GRAY +" - Renames a specified rank.");
                   			player.sendMessage(ChatColor.RED + "/team rankset <playername> <ranknumber>"+ChatColor.GRAY +" - Sets the rank of a team member.");
                   			player.sendMessage(ChatColor.RED + "/team rankmoveall <oldranknumber> <newranknumber>"+ChatColor.GRAY +" - Moves all members of a rank to a new rank.");
                   			player.sendMessage(ChatColor.RED + "/team rankinfo <ranknumber>"+ChatColor.GRAY +" - Outputs a rank's permissions.");
                   			player.sendMessage(ChatColor.RED + "/team rankpermission <ranknumber> <kick/teamchat/rankedit/invite/promote> <true/false>"+ChatColor.GRAY +" - Sets a rank's permissions.");
                   			player.sendMessage(ChatColor.RED + "/team rankdelete <ranknumber>"+ChatColor.GRAY +" - Deletes a rank.");
                   		}
                   		else if(args[1].equalsIgnoreCase("4")) {
                   			player.sendMessage(ChatColor.RED + "Team Area Commands:");
                   		}
                   		else
                   			player.sendMessage(ChatColor.RED + "Improper use of command, Usage is /team help [1-4] to view each page.");
            			
            			break;
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
   			 	if(!tPlayer.hasTeam()) {
   			 		player.sendMessage(ChatColor.RED + "You are not on a team.");
   			 		return true;
   			 	}
   			 	else if(!getRank(PlayerName).canTeamChat()) {
   			 		player.sendMessage(ChatColor.RED + "You lack sufficient permissions to talk in team chat.");
   			 		return true;
   			 	}
   			 	else if (args.length < 1) {
   			 		player.sendMessage(ChatColor.RED + "You did not enter a message to send.");
   			 		return true;
   			 	}
   			 	else {
     				int i;
     				String message = args[0];
     				for(i=1;i<args.length;i++)
     					message += " " + args[i];
	  				String teamKey = tPlayer.getTeamKey();
	  				Team team = Teams.get(tPlayer.getTeamKey());
	  				Player[] onlineList = getServer().getOnlinePlayers();  				 
	  				 
	  				for (Player p : onlineList) {
	  					String userTeamKey = Users.get(p.getDisplayName()).getTeamKey();
	  					if(userTeamKey.equals(teamKey))
	  						p.sendMessage(ChatColor.GREEN + "[TEAM] " +PlayerName + ": " + message);
	  				 }         				 
   			 	}
            }
            else if(commandName.equals("elo"))//maybe change ELO to ratings?
            {
            	if(args[0].toUpperCase() == "LIST")
            	{
            		
            	}
            	else
            	{
            		//Must be a player name
            	}
            }
            else if(commandName.equals("rules"))
            {
            	player.sendMessage(ChatColor.RED + "Rules:");
            	player.sendMessage(ChatColor.RED + "1. Do not use cheats or client modifications that provide you with an unfair advantage.");
            	player.sendMessage(ChatColor.RED + "2. Do not log out in order to avoid combat with another player.");
            	player.sendMessage(ChatColor.RED + "3. Do not spam chat.");
            	player.sendMessage(ChatColor.RED + "Allowed: Total destruction, looting, and killing.");
            }
            else
            {
            	return true;
            }
        return true;    
        }   
        else
        {
        	return true;
        }
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
    			   if(Tier.get("Members") != null){
    				   HashSet<String> Mems = new HashSet<String>((ArrayList<String>)Tier.get("Members"));
    			   
    				   for(String PlayerName : Mems)
    					   Users.get(PlayerName).setTeamKey(key);
    			   
    				   //Add Tier to TeamList
    				   TeamList.add(new TierList(newRank, Mems));
    			   }
    			   else
    				   TeamList.add(new TierList(newRank, new HashSet<String>()));
    		   }
    		   //Add to Teams
    		   Teams.put(key, new Team(TeamList, MOTD, Score, Tag, Color));
    		   
    		   //TODO: Add Team Area Info
    	   }
       }
	}
	private void saveTeams()
	{
		//Print Clans to File.
		try{
			FileWriter fstream = new FileWriter(TeamsFile, false);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("");
			for(String key : Teams.keySet())
			{
				out.write("\'"+ key + "\':\n");
				out.write(Teams.get(key).getSaveString());
			}
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	private void savePlayers()
	{
		try{
			FileWriter fstream = new FileWriter(PlayersFile, false);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("");
			for(String key : Users.keySet())
			{
				out.write("\'"+key + "\': " + Users.get(key).getSaveString()+"\n");
			}
			out.close();
			fstream.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	private TeamRank getRank(String PlayerName)
	{
		TeamPlayer tPlayer = Users.get(PlayerName);
		return Teams.get(tPlayer.getTeamKey()).getRank(PlayerName);
	}
	public TeamPlayer getTeamPlayer(String PlayerName)
	{
		return Users.get(PlayerName);
	}
	public Team getTeam(String PlayerName)
	{
		TeamPlayer tPlayer = Users.get(PlayerName);
		return Teams.get(tPlayer.getTeamKey());
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
	
	private boolean isInteger( String input )  
 	{  
 		try  {  
 			Integer.parseInt( input );  
 			return true;  
 		}  
 		catch( Exception e )  {  
 			return false;  
 		}  
 	} 
	
	public boolean hasUser(String PlayerName)
	{
		return Users.containsKey(PlayerName);
	}
	public void makeUser(String PlayerName)
	{
		Users.put(PlayerName, new TeamPlayer());
		savePlayers();
	}
	public void updateUserDate(String PlayerName)
	{
		Users.get(PlayerName).updateLastSeen();
		savePlayers();
	}



}