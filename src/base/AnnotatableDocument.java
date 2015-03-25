package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnnotatableDocument {
	private Document doc;
	private Hashtable<Integer, SectionContainer> sections;
	
	private Hashtable<String, ArrayList<Integer> > informalSectionIndex = new Hashtable<String, ArrayList<Integer> >();
	private ArrayList<String> informalSections = new ArrayList<String>();
	private EntManager eManager;
	private Hashtable<String,Integer> metaData;
	
	private String textSnapShot;
	
	private EntList italicsList;
	
	
	/** Returns a AnnotateableDocument containing the plain text (space normed) and labeled sections.
	 * No Annotations. 
	 * @param d The javax document from which the AD is built
	 * @param type The informal name for this AD (for intuitive differentiation)*/
	AnnotatableDocument(Document d, String type)
	{
		sections = new Hashtable<Integer, SectionContainer>();
//		doc = (Document) d;
//		String uncleanedText = d.getElementsByTagName("article").item(0).getTextContent();
//		textSnapShot = clean(uncleanedText);
//		getSections(d);
		
		eManager = new EntManager( d.getLocalName() );
		metaData = new Hashtable<String, Integer>();
		italicsList = new EntList( "italics" );
		
	
		
		
		
	}
	
	
	public Hashtable<Integer, SectionContainer> getSections() 
	{
		return sections;
	}
	public Document getDocument()
	{
		return doc;
	}
	
	
	
	/////////////////////////////
	//TODO: deprecate these ArrayList getters
	public ArrayList<Entity> getEntList()
	{
		return eManager.getEntList().getEntList();
	}
	//TODO: deprecate these ArrayList getters
	public ArrayList<Entity> getItalicsList()
	{
		return italicsList.getEntList();
	}
	////////////////////////
	
	
	//TODO: hardcoded for regular and italics ... implement a HT for different rules lists?
	// plus, don't want to return a handle to the list (caller will mess it up)
	public ArrayList<Entity> getEnts(String whichList)
	{
		if(whichList.equals("italics"))
		{
			return italicsList.getEntList();
		}
		return eManager.getEntList().getEntList();
	}
	
	public String getFullText()
	{
		return textSnapShot;
	}
	
	public void addEntity(Entity ent, String whichList)
	{
//		if(whichList.equals("italics"))
//		{
//			italicsList.addEnt(ent);
//			return;
//		}
		
		eManager.addEnt(ent);
	}
	
	public void removeEntity(String ent)
	{
		eManager.getEntList().removeEnt(ent);
	}
	public void addMetaData(String category)
	{
		if(!metaData.contains(category) )
		{
			metaData.put(category, 1);
			return;
		}
		int frequency = metaData.get(category);
		metaData.put(category, ++frequency);
	}
	
	public Hashtable<String, Integer> getMetaData()
	{
		return metaData;
	}
	
	
	//TODO: hmmmm?
	public void consolodateAdjacents()
	{
		for(int i=0; i<sections.size(); i++)
		{
			SectionContainer sc = sections.get(i);
//			System.out.println(sc.getText() +"\n contains");
//			System.out.println( eList.getBySection(sc.getSectionNumber())+" and " + italicsList.getBySection(sc.getSectionNumber()) + "\n" );
			
			ArrayList<Entity> reg = eManager.getEntList().getBySection(sc.getSectionNumber());
			ArrayList<Entity> it = italicsList.getBySection(sc.getSectionNumber());
			
//			consolodate(reg);
//			consolodate(it);
			
			
			
		}
	}

	public EntManager getEntManager() {
		return eManager;
	}
	
	public ArrayList<String> getInformalSectionLookup()
	{
		return informalSections;
	}
	public ArrayList<Integer> getSectionsByInformalName(String informalName)
	{
		return informalSectionIndex.get(informalName);
	}
	
//	private void consolodate(ArrayList<Entity> list)
//	{
//		if(list==null) return;
//		
//		int last = (int)list.get(0).getPosition();
//		ArrayList<Entity> consoldateThese = new ArrayList<Entity>();
//		for(Entity e : list)
//		{
//			if(e.getPosition() == last+1)
//			{
//				
//			}
//		}
//		
//		
//	}

	
	
}
