package com.Kingdoms.Clans;

public class TeamArea {

	String AreaName;
	String Holder;
	String world;
	int xLoc;
	int zLoc;
	int AreaRadius;
	
	//Upgrades
	boolean IntruderAlert;
	boolean BlockResistance;
	boolean BlockDestroyDamage;
	boolean AreaCleanse;
	
	public TeamArea(String aN, int X, int Z, String worldname, int rad, String hold, boolean upIA, boolean upBR, boolean upBDD, boolean upAC)
	{
		AreaName = aN;
		xLoc = X;
		zLoc = Z;
		AreaRadius = rad;
		Holder = hold;
		world = worldname;
		
		//Upgrades
		IntruderAlert = upIA;
		BlockResistance = upBR;
		BlockDestroyDamage = upBDD;
		AreaCleanse = upAC;
	}
	public TeamArea(String aN, int X, int Z, int rad, String hold)
	{
		AreaName = aN;
		xLoc = X;
		zLoc = Z;
		AreaRadius = rad;
		Holder = hold;
		
		//Upgrades
		IntruderAlert = false;
		BlockResistance = false;
		BlockDestroyDamage = false;
		AreaCleanse = false;;
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
		save += "    Z: \"" + "zLoc" + "\"\n";
		save += "    Radius: \"" + AreaRadius + "\"\n";
		save += "    Holder: \"" + Holder + "\"\n";
		save += "    Upgrades: {" +"IntruderAlert: "+ IntruderAlert  + ", BlockResistance: "+ BlockResistance + ", BlockDestroyDamage: "+ BlockDestroyDamage + ", AreaCleanse: "+ AreaCleanse + "}\n";
		
		return save;
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
	public boolean hasIntruderAlert() {
		return IntruderAlert;
	}
	public void setIntruderAlert(boolean intruderAlert) {
		IntruderAlert = intruderAlert;
	}
	public boolean hasBlockResistance() {
		return BlockResistance;
	}
	public void setBlockResistance(boolean blockResistance) {
		BlockResistance = blockResistance;
	}
	public boolean hasBlockDestroyDamage() {
		return BlockDestroyDamage;
	}
	public void setBlockDestroyDamage(boolean blockDestroyDamage) {
		BlockDestroyDamage = blockDestroyDamage;
	}
	public boolean hasAreaCleanse() {
		return AreaCleanse;
	}
	public void setAreaCleanse(boolean areaCleanse) {
		AreaCleanse = areaCleanse;
	}
	public String getWorld() {
		return world;
	}
	public String getAreaName()
	{
		return AreaName;
	}
	
}
