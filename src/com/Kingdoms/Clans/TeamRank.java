package com.Kingdoms.Clans;

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
	public boolean CanSetRanks() {
		return canSetRanks;
	}
	public void setCanSetRanks(boolean canSetRanks) {
		this.canSetRanks = canSetRanks;
	}
	public boolean CanInvite() {
		return canInvite;
	}
	public void setCanInvite(boolean canInvite) {
		this.canInvite = canInvite;
	}
	public boolean CanEditRanks() {
		return canEditRanks;
	}
	public void CanEditRanks(boolean canEditRanks) {
		this.canEditRanks = canEditRanks;
	}
	public boolean CanKick() {
		return canKick;
	}
	public void setCanKick(boolean canKick) {
		this.canKick = canKick;
	}
	public boolean CanTeamChat() {
		return canTeamChat;
	}
	public void setCanTeamChat(boolean canTeamChat) {
		this.canTeamChat = canTeamChat;
	}
	public boolean CanSeeAreaInfo() {
		return canSeeAreaInfo;
	}
	public void setCanSeeAreaInfo(boolean canSeeAreaInfo) {
		this.canSeeAreaInfo = canSeeAreaInfo;
	}
	
	
	
	
}
