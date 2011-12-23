package com.Kingdoms.Clans;

import java.util.Calendar;

public class TeamPlayer {
	
	private int ELO;
	Calendar LastSeen;
	
	private String TeamKey;
	private String Invite;
	
	//For Loading from a file at start up
	TeamPlayer(int ELOin, Calendar LastSeenin)
	{
		ELO = ELOin;
		LastSeen = LastSeenin;
		TeamKey = "";
		Invite = "";
	}
	//When player joins for the first time
	TeamPlayer()
	{
		ELO = 0;
		LastSeen = getCurrentDate();
		TeamKey = "";
		Invite = "";
	}
	
	private Calendar getCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        return cal;
	}
	
}
