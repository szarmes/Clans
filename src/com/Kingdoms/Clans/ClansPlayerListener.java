package com.Kingdoms.Clans;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class ClansPlayerListener extends PlayerListener {
    public Clans plugin;
    Logger log = Logger.getLogger("Minecraft");
    
    public ClansPlayerListener(Clans instance) {
        plugin = instance;
    }
    public void onPlayerChat(PlayerChatEvent event)
    {
    	if(!event.isCancelled())
    	{
    		Player p = event.getPlayer();
    		String fulltag = "{CLANCOLOR}[{CLANTAG}] ";
    		String format = "{PLAYER} {FULLTAG}{WHITE}: {MSG}";
    		event.setFormat(insertData(format,fulltag,p.getDisplayName()));
    	}
    }
    public void onPlayerJoin(PlayerJoinEvent event){
    	String PlayerName = event.getPlayer().getDisplayName();
    	if(!plugin.hasUser(PlayerName))
    		plugin.makeUser(PlayerName);
    	else
    		plugin.updateUserDate(PlayerName);
    		//add new player
    }
    private String insertData(String format, String tag, String PlayerName)
    {
    	format = format.replace("{PLAYER}", "%1$s");
    	format = format.replace("{MSG}", "%2$s");
    	
    	TeamPlayer tPlayer = plugin.getTeamPlayer(PlayerName);
    	Team team = plugin.getTeam(PlayerName);
    	
    	if(tPlayer.hasTeam()) {
        	tag = tag.replace("{CLANCOLOR}", ""+team.getColor());
        	tag = tag.replace("{CLANTAG}", ""+team.getTeamTag());
        	format = format.replace("{FULLTAG}", tag);
    	}
    	else {
        	format = format.replace("{FULLTAG}", "");
    	}
    	
    	//COLORS
    	format = format.replace("{WHITE}", ""+ChatColor.WHITE);
    	//add rest later
    	
    	return format;
    }

}