package base;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnnotatableDocument {
	private Document doc;
	private Hashtable<Integer, SectionContainer> sections;
	private int special_number; //serves as a unique counter for sectionContainers added to sections
	private Hashtable<String, ArrayList<Integer> > informalSectionIndex = informalSectionIndex = new Hashtable<String, ArrayList<Integer> >();
	private ArrayList<String> informalSections = new ArrayList<String>();
	private EntManager eManager;
	private Hashtable<String,Integer> metaData;
	
	private String textSnapShot;
	
	private EntList italicsList;
	
	
	
	AnnotatableDocument(Node d, String type)
	{
		sections = new Hashtable<Integer, SectionContainer>();
		doc = (Document) d;
		String uncleanedText = doc.getElementsByTagName("article").item(0).getTextContent();
		textSnapShot = clean(uncleanedText);
		getSections(d);
		
		eManager = new EntManager( d.getLocalName() );
		metaData = new Hashtable<String, Integer>();
		italicsList = new EntList( "italics" );
		
		
		
	}
	
	private String clean(String unCleaned)
	{
		
		StringBuilder sb = new StringBuilder();
		for(String t : unCleaned.split(" "))
		{
			if(! (t.equals(" ") || t.equals("")) )
			{
				sb.append(t.trim());
				sb.append(" ");
			}
		}
			
		return sb.toString();
	}
	
	private void getSections(Node d)
	{
		ArrayList<String> path = new ArrayList<String>();
		path.add(d.getNodeName());

		
		loadup(d, path, "UNASSIGNED");
		special_number = 0;
	}
	private void loadup(Node node, ArrayList<String> path, String informalSectionName)
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
		
				
					
				informalSectionName = getInformalSectionName(tempNode,path,informalSectionName);
					
				loadup(tempNode, path, informalSectionName);
			
				// associate the section number with a SectionContainer with copy of node and the path.
				// deep copied the node to separate the text and attribute data I want from ""s left to remedy double counting entities
				if(!tempNode.getTextContent().equals(""))
				{
					int relevance = getSectionRelevance(path);
					sections.put(special_number, new SectionContainer(
							tempNode.cloneNode(true), path, special_number, relevance));
					
					if(informalSections.isEmpty())
					{
						informalSections.add(informalSectionName);
					}
					if(!informalSections.get(informalSections.size()-1).equals(informalSectionName))
					{
						informalSections.add(informalSectionName);
					}
					
					markInformalSection(informalSectionName);
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
	private int getSectionRelevance(ArrayList<String> path)
	{
		if(path.contains("abstract")) return 5; // 5 most important
		if(path.contains("back")) return 0; // 0 means no entity importance
		return 3;
	}
	private String getInformalSectionName(Node tempNode, ArrayList<String> path, String currentName)
	{
		if(path.contains("abstract")) return "abstract";
		
		
		String nodeName = tempNode.getNodeName();
		if(nodeName.equalsIgnoreCase("sec")
				|| nodeName.equalsIgnoreCase("section"))
		{
			NamedNodeMap attributes = tempNode.getAttributes();

			Node sectionType = attributes.getNamedItem("sec-type");
			if(sectionType!=null)
			{
				return sectionType.getNodeValue();
			}
		}
		
		if(path.contains("sec") || path.contains("section") )
		{
			return currentName;
		}
		
		return "UNASSIGNED";
	}
	//baaaad code. TODO: just make a "LookupList" data structure
	private void markInformalSection(String informalSectionName)
	{
		if(!informalSectionIndex.containsKey(informalSectionName))
		{
			informalSectionIndex.put(informalSectionName, new ArrayList<Integer>());
		}
		informalSectionIndex.get(informalSectionName).add(special_number);
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
