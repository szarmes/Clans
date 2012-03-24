package com.Kingdoms.Clans;

import java.util.Calendar;


public class TeamPlayer {

	private int ELO;
	private Calendar LastSeen;

	private String TeamKey;
	private String Invite;
	
	private boolean canTeamKill;
	
	
	//For Loading from a file at start up
	TeamPlayer(int ELOin, Calendar LastSeenin, boolean canTC)
	{
		ELO = ELOin;
		LastSeen = LastSeenin;
		TeamKey = "";
		Invite = "";
		canTeamKill = canTC;
	}
	//When player joins for the first time
	TeamPlayer(boolean canTC)
	{
		ELO = 1400;
		LastSeen = getCurrentDate();
		TeamKey = "";
		Invite = "";
		canTeamKill = canTC;
	}
	public boolean canTeamKill() {
		return canTeamKill;
	}
	public void setCanTeamKill(boolean canTeamKill) {
		this.canTeamKill = canTeamKill;
	}
	public boolean hasTeam()
	{
		return !(TeamKey.equalsIgnoreCase(""));
	}
	public boolean hasInvite()
	{
		return !(Invite.equalsIgnoreCase(""));
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
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DATE));
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
	public String getSaveString()
	{
		String save = "";
		String date = "LastOnline: '" + LastSeen.get(Calendar.MONTH)+"/"+LastSeen.get(Calendar.DATE)+"/"+LastSeen.get(Calendar.YEAR)+"'";
		save = "{" + date +", " + "ELO: '" + ELO + "'}";
		
		
		return save;
	}
}