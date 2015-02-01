package base;
import java.util.ArrayList;
import java.util.Hashtable;


public class EntList {
	
	private String whichList;
	private ArrayList<Entity> entList;
	private Hashtable<String, Integer> nameIndex;
	private Hashtable<String, Integer> sectionIndex;
	
	//setters intentionally omitted
	public String getListName() {
		return whichList;
	}
	public ArrayList<Entity> getEntList() {
		return entList;
	}

	//currently (1.19.15) the only thing calling this is EntManager
	//with name "default:EntManager"
	EntList(String name)
	{
		whichList = name;
		entList = new ArrayList<Entity>();
		nameIndex = new Hashtable<String, Integer>();
		sectionIndex = new Hashtable<String, Integer>();
	}
	
	public Entity getByIndex(int index)
	{
		return entList.get(index);
	}
	public Entity getByName(String name)
	{
		int index = nameIndex.get(name);
		return entList.get(index);
	}
	public Entity getBySection(String section)
	{
		int index = sectionIndex.get(section);
		return entList.get(index);
	}
	public Entity getBySection(SectionContainer section)
	{
		String strSection = section.toString();
		int index = nameIndex.get(strSection);
		return entList.get(index);
	}
	
	public void addEnt(Entity ent)
	{
		int nextIndex = entList.size();
		
		//TODO: need to deal with collisions :( 
		nameIndex.put(ent.getText(), nextIndex);
		sectionIndex.put(ent.getLocation().toString(), nextIndex);
		
		entList.add(ent);
		
	}
	
	public void removeEnt(String ent)
	{
		// go backwards to do this in one pass without messing up the indexing
		for(int i = entList.size()-1; i >=0; i--)
		{
			if(entList.get(i).getText().equals(ent))
			{
				entList.remove(i);	
			}
		}
		
	}
	
}
