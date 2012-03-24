package com.Kingdoms.Clans;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import org.bukkit.Location;

public class TeamArea {

	private String AreaName;
	private String Holder;
	private String world;
	private int xLoc;
	private int zLoc;
	private int AreaRadius;
	
	//Upgrades
	private boolean UpgradeAlerter;
	private boolean UpgradeResistance;
	private boolean UpgradeDamager;
	private boolean UpgradeCleanser;
	
	//Upgrades Variables
	private int LastAlertTime;
	private HashSet<Location> Cleanser = new HashSet<Location>();
	private HashSet<String> DamagerKeys = new HashSet<String>();
	private int LastOnlineTime;
	
	public TeamArea(String aN, int X, int Z, String worldname, int rad, String hold, boolean upIA, boolean upBR, boolean upBDD, boolean upAC)
	{
		AreaName = aN;
		xLoc = X;
		zLoc = Z;
		AreaRadius = rad;
		Holder = hold;
		world = worldname;
		
		LastAlertTime = getTime();
		LastOnlineTime = getTime();
		
		//Upgrades
		UpgradeAlerter = upIA;
		UpgradeResistance = upBR;
		UpgradeDamager = upBDD;
		UpgradeCleanser = upAC;
	}
	public TeamArea(String aN, int X, int Z, String worldname, int rad, String hold)
	{
		AreaName = aN;
		xLoc = X;
		zLoc = Z;
		AreaRadius = rad;
		Holder = hold;
		world = worldname;
		
		LastAlertTime = getTime();
		LastOnlineTime = getTime();
		
		//Upgrades
		UpgradeAlerter = false;
		UpgradeResistance = false;
		UpgradeDamager = false;
		UpgradeCleanser = false;
	}
	public boolean inArea(int x, int z, String worldname)
	{		        
		boolean isInArea = false;
		if(world.equalsIgnoreCase(worldname)) {
			if(xLoc-AreaRadius <= x && x <= xLoc+AreaRadius) {
	    		if(zLoc-AreaRadius <= z && z <= zLoc+AreaRadius) {
	    			isInArea = true;
	    		}
			}
		}
		return isInArea;
	}
	public String getSaveString()
	{
		String save = "";
		save += "    Type: \"" + "Clan" + "\"\n";
		save += "    Name: \"" + AreaName + "\"\n";
		save += "    World: \"" + world + "\"\n";
		save += "    X: \"" + xLoc + "\"\n";
		save += "    Z: \"" + zLoc + "\"\n";
		save += "    Radius: \"" + AreaRadius + "\"\n";
		save += "    Holder: \"" + Holder + "\"\n";
		save += "    Upgrades: {" +"IntruderAlert: "+ UpgradeAlerter  + ", BlockResistance: "+ UpgradeResistance + ", BlockDestroyDamage: "+ UpgradeDamager + ", AreaCleanse: "+ UpgradeCleanser + "}\n";
		
		return save;
	}
	private int getTime()
	{
		Calendar calendar = new GregorianCalendar();
		int time = calendar.get(Calendar.HOUR)*1000 + calendar.get(Calendar.MINUTE)*100 + calendar.get(Calendar.SECOND);
		return time;
	}
	public void updateAlertTime()
	{
		LastAlertTime = getTime();
	}
	public int getLastAlertTime()
	{
		return LastAlertTime;
	}
	public String getHolder() {
		return Holder;
	}
	public void setHolder(String holder) {
		Holder = holder;
	}
	public int getxLoc() {
		return xLoc;
	}
	public void setxLoc(int xLoc) {
		this.xLoc = xLoc;
	}
	public int getzLoc() {
		return zLoc;
	}
	public void setzLoc(int zLoc) {
		this.zLoc = zLoc;
	}
	public int getAreaRadius() {
		return AreaRadius;
	}
	public void setAreaRadius(int areaRadius) {
		AreaRadius = areaRadius;
	}
	public boolean hasUpgradeAlerter() {
		return UpgradeAlerter;
	}
	public boolean hasUpgradeResistance() {
		return UpgradeResistance;
	}
	public boolean hasUpgradeDamager() {
		return UpgradeDamager;
	}
	public boolean hasUpgradeCleanser() {
		return UpgradeCleanser;
	}
	public void setUpgradeAlerter(boolean upgradeAlerter) {
		UpgradeAlerter = upgradeAlerter;
	}
	public void setUpgradeResistance(boolean upgradeResistance) {
		UpgradeResistance = upgradeResistance;
	}
	public void setUpgradeDamager(boolean upgradeDamager) {
		UpgradeDamager = upgradeDamager;
	}
	public void setUpgradeCleanser(boolean upgradeCleanser) {
		UpgradeCleanser = upgradeCleanser;
	}
	public String getWorld() {
		return world;
	}
	public String getAreaName()
	{
		return AreaName;
	}
	public void increaseRadius(int i) {
		AreaRadius += i;
	}
	public void addCleanseLocation(Location loc)
	{
		Cleanser.add(loc);
	}
	public void removeCleanseLocation(Location loc)
	{
		Cleanser.remove(loc);
	}
	public boolean hasCleanseLocation(Location loc)
	{
		return Cleanser.contains(loc);
	}
	public HashSet<Location> getCleanseData() {
		return Cleanser;
	}
	public boolean hasDamagerKey(String playername) {
		return DamagerKeys.contains(playername);
	}
	public void addDamagerKey(String playername) {
		DamagerKeys.add(playername);
	}
	public int getLastOnlineTime() {
		return LastOnlineTime;
	}
	public void setLastOnlineTime() {
		LastOnlineTime = getTime();
	}
	public void removeAllDamagerKeys() {
		DamagerKeys = new HashSet<String>();
	}
	
}
