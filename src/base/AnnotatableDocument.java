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
	
	public EntList eList;
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
				
//				/// repeated path remedy?
//				if(pathRepeat(path))
//				{
//					ArrayList<String> lastPath = getSections().get(special_number-1).getPath();
//					String lastPathTopNode = lastPath.get(lastPath.size()-1);
//					
//					int lastNumber = 0;
//					if(lastPathTopNode.matches("[^-]+?[0-9]+"))
//					{
//						lastNumber = Integer.valueOf(lastPathTopNode.replace("[^\\-]+", ""));
//					}
//					String currentPathTopNode = path.get(path.size()-1);
//					if(lastNumber == 0)
//					{
//						currentPathTopNode = currentPathTopNode+"-2";
//						path.set(path.size()-1, currentPathTopNode);
//					}
//					else{ path.set(path.size()-1, currentPathTopNode+(lastNumber+1));}
//				}
//				///
				
				loadup(tempNode, path );
			
				// associate the section number with a SectionContainer with copy of node and the path.
				// deep copied the node to separate the text and attribute data I want from ""s left to remedy double counting entities
				if(!tempNode.getTextContent().equals(""))
				{
					getSections().put(special_number, new SectionContainer(tempNode.cloneNode(true), path, special_number));
					special_number++;
				}
				// clear out the text at the bottom of the recrusion tree as to not double count as we ascend back up
				tempNode.setTextContent("");
			
				//"pop" off the last layer when you are done with it's children
				if(path.size()>0) path.remove( path.size()-1 );
				
			}	
		}		
	}
	
	private boolean pathRepeat(ArrayList<String> current)
	{
		if(special_number==0){return false;}
		ArrayList<String> last = getSections().get(special_number-1).getPath();
		String last_topNode = last.get(last.size()-1);
		int number_last = 0;
		if(last_topNode.contains("-\\d+") || last_topNode.contains("-\\d+"))
		{
			number_last = Integer.valueOf( last_topNode.replaceAll("[^-]+", "") );
			//last.remove(current.size()-1);
		}
		
		if(number_last > 1)
		{
			last.remove(last.size()-1);
		}
		
		if(current.equals(last))
		{
			if(number_last > 1)
			{
				last.add(last_topNode);
			}
			return true;
		}
		return false;
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
		return eList.getEntList();
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
		return eList.getEntList();
	}
	
	public void addEntity(Entity ent, String whichList)
	{
		if(whichList.equals("italics"))
		{
			italicsList.addEnt(ent);
			return;
		}
		eList.addEnt(ent);
	}
	
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
