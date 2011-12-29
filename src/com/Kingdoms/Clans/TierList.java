package com.Kingdoms.Clans;

import java.util.HashSet;

public class TierList {
	
	private TeamRank Rank;
	private HashSet<String> RankMembers;
	
	//Newly Created
	public TierList(TeamRank r)
	{
		Rank = r;
		RankMembers = new HashSet<String>();
	}
	//Read in from File
	public TierList(TeamRank r, HashSet<String> list)
	{
		Rank = r;
		RankMembers = list;
	}
	public void add(String PlayerName)
	{
		RankMembers.add(PlayerName);
	}
	public void remove(String PlayerName)
	{
		RankMembers.remove(PlayerName);
	}
	public boolean containsMember(String PlayerName)
	{
		return RankMembers.contains(PlayerName);
	}
	public boolean isEmpty()
	{
		return RankMembers.isEmpty();
	}
	public TeamRank getRank() {
		return Rank;
	}
	public void setRank(TeamRank rank) {
		Rank = rank;
	}	
	public int getTierSize()
	{
		return RankMembers.size();
	}
	public String getSaveString()
	{
		String save = Rank.getSaveString();
		save += "            Members:\n";
		for(String player : RankMembers)
		{
			save += "                - " + player + "\n";
		}
		return save;
	}
	public String membersToString()
	{
		String list = "";
		for(String member : RankMembers)
		{
			list += member + ", ";
		}
		list.substring(0,list.length()-2);
		return list;
	}
}
