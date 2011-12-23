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
