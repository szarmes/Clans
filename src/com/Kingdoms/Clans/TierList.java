package com.Kingdoms.Clans;

import java.util.HashSet;

public class TierList {
	
	TeamRank rank;
	HashSet<String> RankMembers;
	public TierList(TeamRank r)
	{
		rank = r;
		RankMembers = new HashSet<String>();
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
		return rank;
	}
	public void setRank(TeamRank rank) {
		this.rank = rank;
	}	
}
