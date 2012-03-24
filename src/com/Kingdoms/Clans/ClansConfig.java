package com.Kingdoms.Clans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

public class ClansConfig {
	
	//General
	private int Currency; //Added
	private boolean UseScore; //Not Needed Yet
	private boolean UseELO; //Not Needed Yet
	private boolean AllowTKToggle; //Added
	private boolean TeamTKDefault; //Added
	
	//Chat
	private boolean UseTags; // Added
	private String TagFormat; //Added
	private String MessageFormat; //Added
	
	//Areas
	private boolean UseAreas; //Not Needed Yet
	private int AreaMaxSize; //Not Needed Yet
	private boolean CapturableAreas; //Not Needed Yet
	private boolean AllowUpgrades; //Not Needed Yet
	private boolean UPIntruderAlert; //Not Needed Yet
	private int AlertThreshold; //Not Needed Yet
	private boolean UPOfflineDamage; //Not Needed Yet
	private int OfflineDamageAmount; //Not Needed Yet
	private int ODCooldown;
	private boolean UPBlockResist; //Not Needed Yet
	private int ResistanceBlock; //Not Needed Yet
	private boolean UPCleanse;
	
	//Costs
	private int TagCost; //Added
	private int AreaCost; //Not Needed Yet
	private int incCapacityCost; //Not Needed Yet
	private int incSizeCost; //Not Needed Yet
	private int UPAlertsCost; //Not Needed Yet
	private int UPDamageCost; //Not Needed Yet
	private int UPResistCost; //Not Needed Yet
	private int UPCleanseCost;
	
	//Population Reqs
	private int ReqMemColor; //Added
	private int ReqMemArea; //Not Needed Yet
	
	//Score Reqs
	private int ReqScoreColor; //Not Needed Yet
	private int ReqScoreCape; //Not Needed Yet
	
	//Clean Up
	private int CleanPlayerDays; //Not Needed Yet
	
	
	public ClansConfig()
	{
		//Set Default Values
		//General
		Currency = 41;
		UseScore = true;
		UseELO = true;
		AllowTKToggle = true;
		TeamTKDefault = false;
		
		//Chat
		UseTags = true;
		TagFormat = "{CLANCOLOR}[{CLANTAG}] ";
		MessageFormat = "{PLAYER} {FULLTAG}{WHITE}: {MSG}";
		
		//Areas
		UseAreas = true;
		AreaMaxSize = 200;
		CapturableAreas = true;
		AllowUpgrades = true;
		UPIntruderAlert = true;
		AlertThreshold = 30;
		UPOfflineDamage = true;
		OfflineDamageAmount = 2;
		ODCooldown = 1000;
		UPBlockResist = true;
		ResistanceBlock = 49;
		UPCleanse = true;
		
		//Costs
		TagCost = 5;
		AreaCost = 10;
		incCapacityCost = 5;
		incSizeCost = 10;
		UPAlertsCost = 10;
		UPDamageCost = 25;
		UPResistCost = 50;
		UPCleanseCost = 100;
		
		//Population Reqs
		ReqMemColor = 10;
		ReqMemArea = 10;
		
		//Score Rank Reqs
		ReqScoreColor = 10;
		ReqScoreCape = 10;
		
		//Clean Up
		CleanPlayerDays = 14;
		
		//setConfig
		setConfig();
	}
	private void setConfig()
	{
		HashMap<String,HashMap<String,Object>> pl = null;
		Yaml yamlCfg = new Yaml();
		Reader reader = null;
        try {
            reader = new FileReader("plugins/Clans/Config.yml");
        } catch (final FileNotFoundException fnfe) {
        	 System.out.println("Config.YML Not Found!");
        	   try{
	            	  String strManyDirectories="plugins/Clans";
	            	  boolean success = (new File(strManyDirectories)).mkdirs();
	            	  }catch (Exception e){//Catch exception if any
	            	  System.err.println("Error: " + e.getMessage());
	            	  }
        } finally {
            if (null != reader) {
                try {
                    pl = (HashMap<String,HashMap<String,Object>>)yamlCfg.load(reader);
                    reader.close();
                } catch (final IOException ioe) {
                    System.err.println("We got the following exception trying to clean up the reader: " + ioe);
                }
            }
        }
        if(pl != null)
        {
        	HashMap<String,Object> General = pl.get("General");
    		//General
    		Currency = (int) General.get("Currency");
    		UseScore = (boolean) General.get("Use Score");
    		UseELO = (boolean) General.get("Use ELO");
    		AllowTKToggle = (boolean) General.get("Allow TK Toggle");
    		TeamTKDefault = (boolean) General.get("Team TK Default");
    		
        	HashMap<String,Object> Chat = pl.get("Chat");
    		//Chat
    		UseTags = (boolean) Chat.get("Use Tags");
    		TagFormat = (String) Chat.get("Tag Format");
    		MessageFormat = (String) Chat.get("Message Format");
    		
        	HashMap<String,Object> Areas = pl.get("Areas");
    		//Areas
    		UseAreas = (boolean) Areas.get("Use Areas");
    		AreaMaxSize = (int) Areas.get("Max Size");
    		CapturableAreas = (boolean) Areas.get("Capturable");
    		AllowUpgrades = (boolean) Areas.get("Allow Upgrades");
    		UPIntruderAlert = (boolean) Areas.get("UP Intruder Alert");
    		AlertThreshold = (int) Areas.get("Alert Threshold");
    		UPOfflineDamage = (boolean) Areas.get("UP Offline Defense");
    		OfflineDamageAmount = (int) Areas.get("Offline Damage");
    		ODCooldown = (int) Areas.get("Damager Cooldown");
    		UPBlockResist = (boolean) Areas.get("UP Block Resist");
    		ResistanceBlock = (int) Areas.get("Resistance Block");
    		UPCleanse = (boolean) Areas.get("UP Cleanser");
    		
        	HashMap<String,Object> Costs = pl.get("Costs");
    		//Costs
    		TagCost = (int) Costs.get("Tag");
    		AreaCost = (int) Costs.get("Area");
    		incCapacityCost = (int) Costs.get("Increase Capacity");
    		incSizeCost = (int) Costs.get("Increase Size");
    		UPAlertsCost = (int) Costs.get("Alert Upgrade");
    		UPDamageCost = (int) Costs.get("Damage Upgrade");
    		UPResistCost = (int) Costs.get("Resist Upgrade");
    		UPCleanseCost = (int) Costs.get("Cleanse Upgrade");
    		
        	HashMap<String,Object> ReqMem = pl.get("Req Member Counts");
    		//Population Reqs
    		ReqMemColor = (int) ReqMem.get("Tag Color");
    		ReqMemArea = (int) ReqMem.get("Area");
    		
        	HashMap<String,Object> ReqScore = pl.get("Req Score Ranks");
    		//Score Rank Reqs
    		ReqScoreColor = (int) ReqScore.get("Color");
    		ReqScoreCape =  (int) ReqScore.get("Cape");
    		
        	HashMap<String,Object> Cleanup = pl.get("Clean Up");
    		//Clean Up
    		CleanPlayerDays = (int) Cleanup.get("Clear Player Days");
        }
		
	}
	public int getCurrency() {
		return Currency;
	}
	public boolean UseScore() {
		return UseScore;
	}
	public boolean UseELO() {
		return UseELO;
	}
	public boolean AllowTKToggle() {
		return AllowTKToggle;
	}
	public boolean TeamTKDefault() {
		return TeamTKDefault;
	}
	public boolean UseTags() {
		return UseTags;
	}
	public String getTagFormat() {
		return TagFormat;
	}
	public String getMessageFormat() {
		return MessageFormat;
	}
	public boolean UseAreas() {
		return UseAreas;
	}
	public int getAreaMaxSize() {
		return AreaMaxSize;
	}
	public boolean CapturableAreas() {
		return CapturableAreas;
	}
	public boolean AllowUpgrades() {
		return AllowUpgrades;
	}
	public boolean isUPIntruderAlert() {
		return UPIntruderAlert;
	}
	public int getAlertThreshold() {
		return AlertThreshold;
	}
	public boolean isUPOfflineDamage() {
		return UPOfflineDamage;
	}
	public int getOfflineDamageAmount() {
		return OfflineDamageAmount;
	}
	public boolean isUPBlockResist() {
		return UPBlockResist;
	}
	public int getTagCost() {
		return TagCost;
	}
	public int getAreaCost() {
		return AreaCost;
	}
	public int getIncCapacityCost() {
		return incCapacityCost;
	}
	public int getIncSizeCost() {
		return incSizeCost;
	}
	public int getUPAlertsCost() {
		return UPAlertsCost;
	}
	public int getUPDamageCost() {
		return UPDamageCost;
	}
	public int getUPResistCost() {
		return UPResistCost;
	}
	public int getReqMemColor() {
		return ReqMemColor;
	}
	public int getReqMemArea() {
		return ReqMemArea;
	}
	public int getReqScoreColor() {
		return ReqScoreColor;
	}
	public int getReqScoreCape() {
		return ReqScoreCape;
	}
	public int getCleanPlayerDays() {
		return CleanPlayerDays;
	}
	public boolean isUPCleanse() {
		return UPCleanse;
	}
	public int getUPCleanseCost() {
		return UPCleanseCost;
	}
	public int getDamagerKeyCooldown() {
		return ODCooldown;
	}
	public int getResistanceBlock() {
		return ResistanceBlock;
	}
	public void setResistanceBlock(int resistanceBlock) {
		ResistanceBlock = resistanceBlock;
	}
	
}
