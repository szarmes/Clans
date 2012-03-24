package com.Kingdoms.Clans;

import org.bukkit.block.BlockState;

public class ResistantBlock implements Runnable{
    public Clans plugin;
    private BlockState state;
    private int ExtTime;
    
    public ResistantBlock(Clans instance, BlockState b) {
        plugin = instance;
        state = b;
        ExtTime = 0;
    }
    public void run() {
    	if(ExtTime == 0)
    		plugin.ResetResistBlock(state.getLocation());
    	else
    		ExtTime--;
    }
	public BlockState getState() {
		return state;
	}
	public int getExtTime() {
		return ExtTime;
	}
	public void IncreaseExtTime() {
		ExtTime++;
	}
    
}
