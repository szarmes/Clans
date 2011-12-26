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
		ELO = 1400;
		LastSeen = getCurrentDate();
		TeamKey = "";
		Invite = "";
	}
	public boolean hasTeam()
	{
		return TeamKey.equalsIgnoreCase("");
	}
	public void setTeamKey(String key)
	{
		TeamKey = key;
	}
	public String getTeamKey() {
		return TeamKey;
	}
	public void clearTeamKey(){
		TeamKey = "";
	}
	public void updateLastSeen()
	{
		LastSeen = getCurrentDate();
	}
	private Calendar getCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        return cal;
	}
	public void setInvite(String invitingTeam){
		Invite = invitingTeam;
	}
	public String getInvite(){
		return Invite;
	}
	public void clearInvite(){
		Invite = "";
	}
}