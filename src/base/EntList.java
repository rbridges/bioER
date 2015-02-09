package base;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


public class EntList {
	
	private String whichList;
	private Hashtable<Integer,Entity> entTable; //HT so that we can remove without ruining indexing
	private int addEntIndex;
	private ArrayList<Entity> entList;
	private Hashtable<String, ArrayList<Integer> > nameIndex;
	private Hashtable<String, ArrayList<Integer> > sectionIndex;
	private Hashtable<Integer, ArrayList<Integer> > integerSectionIndex;
	
	//setters intentionally omitted
	public String getListName() {
		return whichList;
	}
	
	//TODO: not threadsafe?-- there is one global handle to entList, and this changes then distributes it
	public ArrayList<Entity> getEntList() {
		Enumeration<Entity> values = entTable.elements();
		// omitting an equals check, because you'd have to do an O(N) check anyway?
		
		entList.clear();
		while(values.hasMoreElements())
		{
			entList.add( values.nextElement() );
		}
		return entList;
	}

	//currently (1.19.15) the only thing calling this is EntManager
	//with name "default:EntManager"
	EntList(String name)
	{
		whichList = name;
		entTable = new Hashtable<Integer,Entity>();
		addEntIndex = 0;
		
		entList = new ArrayList<Entity>();
		nameIndex = new Hashtable<String, ArrayList<Integer> >();
		sectionIndex = new Hashtable<String, ArrayList<Integer> >();
		
		integerSectionIndex = new Hashtable<Integer, ArrayList<Integer> >();
	}
	
	public Entity getByIndex(int index)
	{
		return entTable.get(index);
	}
	public ArrayList<Entity> getByName(String name)
	{
		ArrayList<Entity> ret = new ArrayList<Entity>();
		ArrayList<Integer> entsWithName = nameIndex.get(name);
		for(Integer i : entsWithName)
		{
			ret.add(entTable.get(i));
		}
		return ret;
//		int index = nameIndex.get(name);
//		return entList.get(index);
	}
	
	
	public ArrayList<Entity> getBySection(String section)
	{
		ArrayList<Entity> ret = new ArrayList<Entity>();
		ArrayList<Integer> entsInSection = sectionIndex.get(section);
		for(Integer i : entsInSection)
		{
			ret.add(entTable.get(i));
		}
		return ret;
	}
	public ArrayList<Entity> getBySection(SectionContainer section)
	{
		return getBySection(section.toString());
	}
//	public ArrayList<Entity> getByUniqueSection(int sectionNumber)
//	{
//		
//	}
	
	public void addEnt(Entity ent)
	{
		
		
		// if this is the first entity with that name, put a new list with that entity
		if(! nameIndex.containsKey(ent.getText()))
		{
			ArrayList<Integer> newList = new ArrayList<Integer>();
			newList.add(addEntIndex);
			nameIndex.put(ent.getText(), newList);
		}
		//otherwise, simply add the ent to the existing list
		else{ nameIndex.get(ent.getText()).add(addEntIndex); }
		
		
		if(! sectionIndex.containsKey(ent.getLocation().toString()))
		{
			ArrayList<Integer> newList = new ArrayList<Integer>();
			newList.add(addEntIndex);
			sectionIndex.put(ent.getText(), newList);
		}
		else{ sectionIndex.get(ent.getLocation().toString()).add(addEntIndex); }
		
		if(! integerSectionIndex.containsKey(ent.getSectionNumber()))
		{
			ArrayList<Integer> newList = new ArrayList<Integer>();
			newList.add(addEntIndex);
			integerSectionIndex.put(ent.getSectionNumber(), newList);
		}
		else{ integerSectionIndex.get(ent.getSectionNumber()).add(addEntIndex); }
		
		
		
		//now that we've indexed by name and section, just add to the master list
		entTable.put(addEntIndex++,ent);
	}
	
	public void removeEnt(String ent)
	{
		ArrayList<Integer> entsWithName = nameIndex.get(ent);
		
		for(Integer i : entsWithName)
		{
			entTable.remove(i);	
		}
		
	}
	
}
