package base;

import java.util.HashSet;
import java.util.Hashtable;

public class AliasManager {
	
	//String name of entity points to the AliasGroup of which it is a part
	Hashtable<String, AliasGroup> table;

	AliasManager()
	{
		table = new Hashtable<String, AliasGroup>();
	}
	
	public void pair(String known, String found)
	{
		if(!table.containsKey( known ))
		{
			AliasGroup group = new AliasGroup();
			group.add(known); // put first entry into new AliasGroup
			group.setMainName(known); //assume this is the main, since its our first encounter
			table.put(known, group); //register this first entry with our lookup table 
		}
		
		//get the 'known' entity's group and stick the 'found' entity in the group (and register the found)
		AliasGroup knownsGroup = table.get( known ); 
		knownsGroup.add(found); 
		table.put(found, knownsGroup); //register for quick lookup (memory inefficient?)
	}
	
	public HashSet<String> getAliases(String name)
	{
		AliasGroup aliasGroup = table.get(name);
		if(aliasGroup!=null)
		{
			return aliasGroup.getAliases();
		}
		else return new HashSet<String>();
	}
	
	public String getMainName(String entityName)
	{

		AliasGroup group = table.get(entityName);
		if (group==null )
		{
			return entityName;
		}
		
		
		return group.getMainName();
	}
}
