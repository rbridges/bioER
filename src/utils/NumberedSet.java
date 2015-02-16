package utils;

import java.util.Hashtable;
import java.util.Set;

public class NumberedSet<T> {
	Hashtable<T,Integer> set;
	int itemCount;
	
	public NumberedSet()
	{
		set = new Hashtable<T,Integer>();
		itemCount = 0;
	}
	
	public void insert(T item)
	{
		if(!set.contains(item))
		{
			set.put(item, 0);
		}
		set.put( item,  set.get(item)+1 );
		itemCount++;
	}
	
	
	public Set<T> getItems()
	{
		return set.keySet();
	}
	
	public int getFrequency(T item)
	{
		return set.get(item);
	}
	
	public int getItemCount()
	{
		return itemCount;
	}
}
