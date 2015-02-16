package utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class NameSet<T> {
	Hashtable<String,ArrayList<T> > set;
	int itemCount;
	
	public NameSet()
	{
		set = new Hashtable<String,ArrayList<T> >();
		itemCount = 0;
	}
	
	public void insert(String name, T item)
	{
		if(!set.containsKey(name))
		{
			ArrayList<T> items = new ArrayList<T>();
			set.put(name, items);
		}
		set.get(name).add(item);
		itemCount++;
	}
	
	
	public Set<String> getItems()
	{
		return set.keySet();
	}
	
	public int getFrequency(String name)
	{
		return set.get(name).size();
	}
	
	public int getItemCount()
	{
		return itemCount;
	}
}
