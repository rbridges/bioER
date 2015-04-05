package base;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;


public class EntList {
	
	private String whichList;
	private Hashtable<Integer,Entity> entTable; //HT so that we can remove without ruining indexing
	private int addEntIndex;
	private ArrayList<Entity> entList;
	private Hashtable<String, ArrayList<Integer> > nameIndex;
	private Hashtable<Integer, ArrayList<Integer> > sectionIndex;
	
	private HashSet<String> uniques;
	
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
	public EntList(String name)
	{
		whichList = name;
		entTable = new Hashtable<Integer,Entity>();
		addEntIndex = 0;
		
		entList = new ArrayList<Entity>();
		nameIndex = new Hashtable<String, ArrayList<Integer> >();
		sectionIndex = new Hashtable<Integer, ArrayList<Integer> >();
		
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
	
	
	public ArrayList<Entity> getBySection(int section)
	{
		ArrayList<Entity> ret = new ArrayList<Entity>();
		ArrayList<Integer> entsInSection = sectionIndex.get(section);
		if(entsInSection == null) return null;
		for(Integer i : entsInSection)
		{
			ret.add(entTable.get(i));
		}
		return ret;
	}
	
//	public ArrayList<Entity> getByUniqueSection(int sectionNumber)
//	{
//		
//	}
	
	public void addEnt(Entity ent)
	{
		// let it carry it's own id, so that others (like EAManager) can ask for it to identify the ent while indexing.
		ent.setId(addEntIndex);
		
		// if this is the first entity with that name, put a new list with that entity
		if(! nameIndex.containsKey(ent.getText()))
		{
			ArrayList<Integer> newList = new ArrayList<Integer>();
			newList.add(addEntIndex);
			nameIndex.put(ent.getText(), newList);
		}
		//otherwise, simply add the ent to the existing list
		else{ nameIndex.get(ent.getText()).add(addEntIndex); }
		
		
		if(! sectionIndex.containsKey(ent.getSectionNumber()))
		{
			ArrayList<Integer> newList = new ArrayList<Integer>();
			newList.add(addEntIndex);
			sectionIndex.put(ent.getSectionNumber(), newList);
		}
		else{ sectionIndex.get(ent.getSectionNumber()).add(addEntIndex); }
		
//		if(! integerSectionIndex.containsKey(ent.getSectionNumber()))
//		{
//			ArrayList<Integer> newList = new ArrayList<Integer>();
//			newList.add(addEntIndex);
//			integerSectionIndex.put(ent.getSectionNumber(), newList);
//		}
//		else{ integerSectionIndex.get(ent.getSectionNumber()).add(addEntIndex); }
		
		
		
		
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
