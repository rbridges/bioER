package l;

import java.util.ArrayList;
import java.util.Hashtable;

import l.EntAnnotation.Status;
import base.EntList;
import base.Entity;

/** A place to index on author annotations */
public class EAManager {
	private EntList falseNegatives;
	private Hashtable<Status, ArrayList<Integer> >  statusIndex;
//	private ArrayList<Entity> falseNegatives;
	EAManager(EntList eList)
	{
//		entListHandle = eList;
		falseNegatives = new EntList("false negatives");
		statusIndex = new Hashtable<Status, ArrayList<Integer> > ();
		statusIndex.put(EntAnnotation.Status.TP, new ArrayList<Integer>());
		statusIndex.put(EntAnnotation.Status.FP, new ArrayList<Integer>());
		statusIndex.put(EntAnnotation.Status.FN, new ArrayList<Integer>());
	}
	
	public void markExisting(Entity ent, Status s)
	{
		statusIndex.get(s).add(ent.getId());
		ent.setStatus(s);
	}
	public void markMissed(Entity ent)
	{
		Status s = EntAnnotation.Status.FN;
		
		// put FN in their own list, owned by EAManager as to separate out entities found by rules
		// and those found by annotating authors, etc.
		falseNegatives.addEnt(ent);
		statusIndex.get(s).add(ent.getId());
		ent.setStatus(s);
	}
	/** Does nothing currently */
	public void makeNote(String _note)
	{
		//TODO: notes?
	}
	
	
}
