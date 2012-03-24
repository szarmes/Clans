package com.Kingdoms.Clans;

import org.bukkit.block.BlockState;

public class ResistantBlock implements Runnable{
    public Clans plugin;
    private BlockState state;
    private int ExtTime;
    private int id;
    
    public ResistantBlock(Clans instance, BlockState b, int idin) {
        plugin = instance;
        state = b;
        ExtTime = 0;
        id = idin;
    }
    public int getID()
    {
    	return id;
    }
    public void run() {
    	if(ExtTime == 0)
    		plugin.ResetResistBlock(state.getLocation(),id);
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
