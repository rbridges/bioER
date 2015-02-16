package base;

import java.util.ArrayList;
import java.util.HashSet;

public class EntManager {

	private AliasManager aliasManager;
	private EntList eList;
	private HashSet<String> uniques;
	
	EntManager(String name)
	{
		eList = new EntList( name );
		aliasManager = new AliasManager();
		uniques = new HashSet<String>();
	}
	
	public void aliasEnts(String master, String alias) 
	{
		aliasManager.pair(master,alias);
	}
	
	public EntList getEntList()
	{
		return eList;
	}
	
	public HashSet<String> getAliases(String name)
	{
		return aliasManager.getAliases(name);
	}
	
	public void addEnt(Entity ent)
	{
		String entName = ent.getText();
		// so that the uniques table only has the main name
		HashSet<String> aliases = aliasManager.getAliases(entName);
		for(String alias : aliases )
		{
			uniques.remove(alias);
		}
		uniques.add( aliasManager.getMainName(entName) );
		
		eList.addEnt(ent);
	}
	
	public HashSet<String> getUniques()
	{
		return uniques;
	}
	
	public String getMainName(String name)
	{
		return aliasManager.getMainName(name);
	}
}
