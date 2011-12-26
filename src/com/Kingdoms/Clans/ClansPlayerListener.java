package com.Kingdoms.Clans;

import java.util.logging.Logger;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class ClansPlayerListener extends PlayerListener {
    public Clans plugin;
    Logger log = Logger.getLogger("Minecraft");
    
    public ClansPlayerListener(Clans instance) {
        plugin = instance;
    }
    
    public void onPlayerJoin(PlayerJoinEvent event){
    	String PlayerName = event.getPlayer().getDisplayName();
    	if(!plugin.hasUser(PlayerName))
    		plugin.makeUser(PlayerName);
    	else
    		plugin.updateUserDate(PlayerName);
    		//add new player
    }

}