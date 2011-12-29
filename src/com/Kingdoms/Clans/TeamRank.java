package com.Kingdoms.Clans;

import java.util.HashMap;

public class TeamRank {
	
	private String RankName;

	//Permissions
	private boolean canSetRanks;
	private boolean canInvite;
	private boolean canEditRanks;
	private boolean canKick;
	private boolean canTeamChat;
	private boolean canSeeAreaInfo;

	
	public TeamRank(String name)
	{
		RankName = name;
		canSetRanks = false;
		canInvite = false;
		canEditRanks = false;
		canKick = false;
		canTeamChat = true;
		canSeeAreaInfo = false;
	
	}
	//Read in from file
	public TeamRank(String name, HashMap<String,Boolean> perms)
	{
		RankName = name;
		canSetRanks = perms.get("SetRanks");
		canInvite = perms.get("Invite");
		canEditRanks = perms.get("EditRanks");
		canKick = perms.get("Kick");
		canTeamChat = perms.get("TeamChat");
		canSeeAreaInfo = perms.get("AreaInfo");
		
	}
	public void makeTopRank()
	{
		canSetRanks = true;
		canInvite = true;
		canEditRanks = true;
		canKick = true;
		canTeamChat = true;
		canSeeAreaInfo = true;
	
	}
	public String getRankName() {
		return RankName;
	}
	public void setRankName(String rankName) {
		RankName = rankName;
	}
	public boolean canSetRanks() {
		return canSetRanks;
	}
	public void setCanSetRanks(boolean canSetRanks) {
		this.canSetRanks = canSetRanks;
	}
	public boolean canInvite() {
		return canInvite;
	}
	public void setCanInvite(boolean canInvite) {
		this.canInvite = canInvite;
	}
	public boolean canEditRanks() {
		return canEditRanks;
	}
	public void setCanEditRanks(boolean canEditRanks) {
		this.canEditRanks = canEditRanks;
	}
	public boolean canKick() {
		return canKick;
	}
	public void setCanKick(boolean canKick) {
		this.canKick = canKick;
	}
	public boolean canTeamChat() {
		return canTeamChat;
	}
	public void setCanTeamChat(boolean canTeamChat) {
		this.canTeamChat = canTeamChat;
	}
	public boolean canSeeAreaInfo() {
		return canSeeAreaInfo;
	}
	public void setCanSeeAreaInfo(boolean canSeeAreaInfo) {
		this.canSeeAreaInfo = canSeeAreaInfo;
	}
	public String getSaveString()
	{
		String save = "";
		
		save += "            Rank Name: '"+RankName+"'\n";
		String perms = "{SetRanks: "+canSetRanks+", " + "EditRanks: "+canEditRanks+", " + "Kick: "+canKick+", " + "TeamChat: "+canTeamChat+", " +"Invite: "+canInvite+", " + "AreaInfo: "+canSeeAreaInfo+"}";
		save += "            Permissions: "+perms+"\n";
		return save;
	}
	
	
}
