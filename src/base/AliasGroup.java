package base;

import java.util.ArrayList;
import java.util.HashSet;

public class AliasGroup {
	//private ArrayList<Entity> instances;
	private String mainName;
	private HashSet<String> aliases;
		
	AliasGroup()
	{
		aliases = new HashSet<String>();
	}
	
	public void add(String addMe)
	{
		
		aliases.add( addMe );
	}
	
	
	/////////////// Getters & Setters

	public String getMainName() {
		return mainName;
	}
	public HashSet<String> getAliases() {
		return aliases;
	}

	public void setMainName(String mainName) {
		this.mainName = mainName;
	}
	/////////////// 
	
}
