package com.Kingdoms.Clans;

public class TeamPlayer {
	
	private int ELO;
	private int LastSeen;
	
	private String TeamKey;
	private String Invite;
	
	private boolean TeamKillSetting;
	
	//For Loading from a file at start up
	TeamPlayer(int ELOin, int LastSeenin)
	{
		ElO = ELOin;
		LastSeen = LastSeenin;
	}
	
	
}
