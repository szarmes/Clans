package com.Kingdoms.Clans;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClansPlayerListener implements Listener {
    public Clans plugin;
    Logger log = Logger.getLogger("Minecraft");
    
    public ClansPlayerListener(Clans instance) {
        plugin = instance;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(PlayerChatEvent event)
    {
    	if(!event.isCancelled())
    	{
    		Player p = event.getPlayer();
    		String fulltag = plugin.getClansConfig().getTagFormat();
    		String format = plugin.getClansConfig().getMessageFormat();
    		event.setFormat(insertData(format,fulltag,p.getDisplayName()));
    	}
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event){
    	String PlayerName = event.getPlayer().getDisplayName();
    	if(!plugin.hasUser(PlayerName))
    		plugin.makeUser(PlayerName);
    	else
    		plugin.updateUserDate(PlayerName);
    		//add new player
    	
    	//If player has team and motd, print it
    	if (!plugin.getTeamsMOTD(PlayerName).equalsIgnoreCase(""))
    		event.getPlayer().sendMessage(plugin.getTeamsMOTD(PlayerName));
    }
    private String insertData(String format, String tag, String PlayerName)
    {
    	format = format.replace("{PLAYER}", "%1$s");
    	format = format.replace("{MSG}", "%2$s");
    	
    	TeamPlayer tPlayer = plugin.getTeamPlayer(PlayerName);
    	Team team = plugin.getTeam(PlayerName);
    	
    	if(tPlayer.hasTeam()) {
    		if(plugin.getTeam(PlayerName).hasTag()) {
    			tag = tag.replace("{CLANCOLOR}", ""+team.getColor());
    			tag = tag.replace("{CLANTAG}", ""+team.getTeamTag());
    			format = format.replace("{FULLTAG}", tag);
    		}
    		else {
    			format = format.replace("{FULLTAG}", "");
    		}
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