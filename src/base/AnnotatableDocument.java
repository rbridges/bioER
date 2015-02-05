package base;

import java.util.ArrayList;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnnotatableDocument {
	private Document doc;
	private Hashtable<Integer, SectionContainer> sections;
	private int special_number; //serves as a unique counter for sectionContainers added to sections
	
	private EntList eList;
	private Hashtable<String,Integer> metaData;
	
	private EntList italicsList;
	
	AnnotatableDocument(Node d, String type)
	{
		sections = new Hashtable<Integer, SectionContainer>();
		getSections(d);
		doc = (Document) d;
		
		eList = new EntList( d.getLocalName() );
		metaData = new Hashtable<String, Integer>();
		
		italicsList = new EntList( "italics" );
	}
	
	private void getSections(Node d)
	{
		ArrayList<String> path = new ArrayList<String>();
		path.add(d.getNodeName());
		loadup(d, path);
		special_number = 0;
	}
	private void loadup(Node node, ArrayList<String> path)
	{
		NodeList nodeList = node.getChildNodes();
		for (int count = 0; count < nodeList.getLength(); count++) {
			
			Node tempNode = nodeList.item(count);
			// make sure it's an "element node" as opposed to Type, Notation, Position, etc. .
			//if (!(tempNode.getNodeType() == Node.ELEMENT_NODE) ) continue;
			
			if (tempNode.hasChildNodes())
			{
				path.add(tempNode.getNodeName());
				loadup(tempNode, path );
			
				// associate the section number with a SectionContainer with copy of node and the path.
				// deep copied the node to separate the text and attribute data I want from ""s left to remedy double counting entities
				if(!tempNode.getTextContent().equals(""))
				{
					getSections().put(special_number, new SectionContainer(tempNode.cloneNode(true), path));
					special_number++;
				}
				// clear out the text at the bottom of the recrusion tree as to not double count as we ascend back up
				tempNode.setTextContent("");
			
				//"pop" off the last layer when you are done with it's children
				path.remove( path.size()-1 );
			}	
		}		
	}
	
	public Hashtable<Integer, SectionContainer> getSections() 
	{
		return sections;
	}
	public Document getDocument()
	{
		return doc;
	}
	public ArrayList<Entity> getEntList()
	{
		return eList.getEntList();
	}
	public ArrayList<Entity> getItalicsList()
	{
		return italicsList.getEntList();
	}
	public void addEntity(){}
	
	public void removeEntity(String ent)
	{
		eList.removeEnt(ent);
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
	
}
