package com.Kingdoms.Clans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.ChatColor;

public class Team {
	
	private ArrayList<TierList> TeamList;
	
	private ChatColor TeamColor;
	private String TeamMOTD;
	private int TeamScore;
	private String TeamTag;
	
	//Read in from file
	public Team(ArrayList<TierList> TLin, String MOTDin, int Scorein, String Tagin, String Colorin)
	{
		TeamList = TLin;
		TeamMOTD = MOTDin;
		TeamScore = Scorein;
		TeamTag = Tagin;
		TeamColor = interpretColor(Colorin);
	}
	//Read in from file
	public Team(String Leadername)
	{
		TeamList = new ArrayList<TierList> ();
		
		//Make Leader Rank and Add Leader to List
		TeamRank LeaderRank = new TeamRank("Leader");
		LeaderRank.makeTopRank();
		TeamList.add(new TierList(LeaderRank));
		TeamList.get(0).add(Leadername);
		
		//Make Member List
		TeamRank MemberRank = new TeamRank("Member");
		TeamList.add(new TierList(MemberRank));
		
		TeamMOTD = "";
		TeamScore = 0;
		TeamTag = "";
		TeamColor = interpretColor("GRAY");
	}
	
	public boolean isLeader(String PlayerName)
	{
		return TeamList.get(0).containsMember(PlayerName);
	}
	public int getLeaderCount()
	{
		return TeamList.get(0).getTierSize();
	}
	public void addMember(String PlayerName)
	{
		int LastRankNumber = TeamList.size()-1;
		TeamList.get(LastRankNumber).add(PlayerName);
	}
	
	public String getMOTD(){
		return TeamMOTD;
	}
	public void setMOTD(String MOTDin){
		TeamMOTD = MOTDin;		
	}
	
	public void removeMember(String PlayerName)
	{
		int RankCount = TeamList.size();
		int i;
		for(i=0; i<RankCount; i++)
		{
			if(TeamList.get(i).containsMember(PlayerName))
				TeamList.get(i).remove(PlayerName);
		}
	}
	public void addRank(TeamRank NewRank)
	{
		TeamList.add(new TierList(NewRank));
	}
	public boolean removeRank(int i)
	{
		if(TeamList.get(i-1).isEmpty())
		{
			TeamList.remove(i-1);
			return true;
		}
		//Members must be removed first
		return false;
	}
	public int getRankCount()
	{
		return TeamList.size();
	}
	public TeamRank getRank(String PlayerName)
	{
		int RankCount = TeamList.size();
		int i;
		
		TeamRank r = new TeamRank("");
		
		for(i=0; i<RankCount; i++)
		{
			if(TeamList.get(i).containsMember(PlayerName))
				return TeamList.get(i).getRank();
		}
		return r;
	}
	public TeamRank getRank(int RankNumber)
	{
		return TeamList.get(RankNumber-1).getRank();
	}
	public ChatColor getColor()
	{
		return TeamColor;
	}
	public ArrayList<String> getTeamInfo()
	{
		ArrayList<String> teamInfo = new ArrayList<String>();
		teamInfo.add(TeamColor + "Team Members: " + getTeamSize());
		int rankNum = 1;
		for(TierList tl : TeamList)
		{
			teamInfo.add(TeamColor + "" + rankNum + ". "+ tl.getRank().getRankName() + ":" + ChatColor.GRAY + tl.membersToString());
			rankNum++;
		}
		return teamInfo;
	}
	public int getTeamSize()
	{
		int TeamSize = 0;
		for(TierList tl : TeamList)
			TeamSize += tl.getTierSize();
		return TeamSize;
	}
	public String getTeamTag(){
		return "";
	}
	
	public void setTeamTag(String Tagin){
		TeamTag = Tagin;
	}
	public String getSaveString()
	{
		String save = "";
		save += "    Tag: '" + TeamTag + "'\n";
		save += "    Color: '" + reverseInterpretColor(TeamColor) + "'\n";
		save += "    Motd: '" + TeamMOTD + "'\n";
		save += "    Score: '" + TeamScore + "'\n";
		save += "    List:\n";
		int i = 0;
		for(i=0; i<TeamList.size(); i++)
		{
			int r = i+1;
			save += "        Rank " + r + ":\n";
			save += TeamList.get(i).getSaveString();
		}
		
		return save;
	}
	private ChatColor interpretColor(String Colorin) {

		ChatColor c;
		     switch(Colorin.toUpperCase())
		     {
		     case "DARK_RED":
		     c = ChatColor.DARK_RED;
		     break;
		     case "RED":
		     c = ChatColor.RED;
		     break;
		     case "DARK_AQUA":
		     c = ChatColor.DARK_AQUA;
		     break;
		     case "AQUA":
		     c = ChatColor.AQUA;
		     break;
		     case "DARK_GREEN":
		     c = ChatColor.DARK_GREEN;
		     break;
		     case "GREEN":
		     c = ChatColor.GREEN;
		     break;
		     case "DARK_BLUE":
		     c = ChatColor.DARK_BLUE;
		     break;
		     case "BLUE":
		     c = ChatColor.BLUE;
		     break;
		     case "DARK_PURPLE":
		     c = ChatColor.DARK_PURPLE;
		     break;
		     case "PURPLE":
		     c = ChatColor.LIGHT_PURPLE;
		     break;
		     case "GOLD":
		     c = ChatColor.GOLD;
		     break;
		     case "YELLOW":
		     c = ChatColor.YELLOW;
		     break;
		     case "BLACK":
		     c = ChatColor.BLACK;
		     break;
		     default:
		     c = ChatColor.GRAY;
		     break;
		     }
		return c;
		}
	private String reverseInterpretColor(ChatColor Colorin) {

		String c = "";
    	switch(Colorin)
    	{
    		case DARK_RED: 
    			c = "DARK_RED";
    			break;
    		case RED: 
    			c = "RED";
    			break;
    		case DARK_AQUA: 
    			c = "DARK_AQUA";
    			break;
    		case AQUA: 
    			c = "AQUA";
    			break;
    		case DARK_GREEN: 
    			c = "DARK_GREEN";
    			break;
    		case GREEN: 
    			c = "GREEN";
    			break;
    		case DARK_BLUE: 
    			c = "DARK_BLUE";
    			break;
    		case BLUE: 
    			c = "BLUE";
    			break;
    		case DARK_PURPLE: 
    			c = "DARK_PURPLE";
    			break;
    		case LIGHT_PURPLE: 
    			c = "LIGHT_PURPLE";
    			break;
    		case GOLD: 
    			c = "GOLD";
    			break;
    		case YELLOW: 
    			c = "YELLOW";
    			break;
    		case BLACK: 
    			c = "BLACK";
    			break;
    		default:
    			c =  "GRAY";
    			break;
    	}
		return c;
	}

}
