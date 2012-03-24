package com.Kingdoms.Clans;

import java.util.logging.Logger;

import net.minecraft.server.Block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClansBlockListener implements Listener{
    public Clans plugin;
    Logger log = Logger.getLogger("Minecraft");
    
    public ClansBlockListener(Clans instance) {
        plugin = instance;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event){
    	if(!event.isCancelled()) {
	    	String test = plugin.findArea(event.getBlock().getX(), event.getBlock().getZ(), event.getBlock().getWorld().getName());
	    	String userTeam = plugin.getTeamPlayer(event.getPlayer().getDisplayName()).getTeamKey();
	    	if(!userTeam.equalsIgnoreCase(test)) {
		    	if(!test.equalsIgnoreCase("")) { //Block Break in Area
		    		TeamArea a = plugin.getArea(test);
		    		if(a.hasUpgradeAlerter())
		    			plugin.TriggerAlerter(test);
		    		if(a.hasUpgradeDamager())
		    			plugin.TriggerDamager(event.getPlayer(),test);
		    		if(a.hasUpgradeCleanser())
		    			plugin.TriggerCleanserPlace(event.getBlock(),test);
		    	}
	    	}
    	}
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event){
    	if(!event.isCancelled()) {
	    	String test = plugin.findArea(event.getBlock().getX(), event.getBlock().getZ(), event.getBlock().getWorld().getName());
	    	String userTeam = plugin.getTeamPlayer(event.getPlayer().getDisplayName()).getTeamKey();
		    if(!test.equalsIgnoreCase("")) { //Block Break in Area
		    	TeamArea a = plugin.getArea(test);
	    		if(a.hasUpgradeCleanser())
	    			plugin.TriggerCleanserBreak(event.getBlock(),test);
		    	if(!userTeam.equalsIgnoreCase(test)) {
		    		if(a.hasUpgradeResistance()) {
		    			//if has block, set cancel
		    			plugin.TriggerResistanceBreak(event.getBlock());
		    			event.setCancelled(true);
		    		}
		    		if(a.hasUpgradeAlerter())
		    			plugin.TriggerAlerter(test);
		    		if(a.hasUpgradeDamager())
		    			plugin.TriggerDamager(event.getPlayer(),test);
		    	}
		    	else
		    	{
		    		if(a.hasUpgradeResistance() && plugin.isResistBlock(event.getBlock().getLocation())) {
		    			//if has block, set cancel
		    			plugin.TriggerResistanceBreak(event.getBlock());
		    			event.setCancelled(true);
		    		}
		    	}
	    	}
    	}
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDamage(BlockDamageEvent event){
    	if(!event.isCancelled()) {
	    	String test = plugin.findArea(event.getBlock().getX(), event.getBlock().getZ(), event.getBlock().getWorld().getName());
	    	String userTeam = plugin.getTeamPlayer(event.getPlayer().getDisplayName()).getTeamKey();
		    if(!test.equalsIgnoreCase("")) { //Block Break in Area
		    	TeamArea a = plugin.getArea(test);
		    	if(!userTeam.equalsIgnoreCase(test)) {
		    		if(a.hasUpgradeResistance())
		    			plugin.TriggerResistanceDamage(event.getBlock());
		    	}
		    	else
		    	{
		    		if(a.hasUpgradeResistance() && plugin.isResistBlock(event.getBlock().getLocation())) {
		    			plugin.ResetResistBlock(event.getBlock().getLocation());
		    		}
		    	}
	    	}
		    
    	}
    }

}
