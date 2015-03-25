package base;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;




public class DocumentParser {
	
	// attributes migrated from annotatable doc
	private int sectionNumber; //serves as a unique counter for sectionContainers added to sections
	private ArrayList<String> informalSections = new ArrayList<String>();
	public Hashtable<Integer, SectionContainer> sections = new Hashtable<Integer, SectionContainer>();
	private Hashtable<String, ArrayList<Integer> > informalSectionIndex = new Hashtable<String, ArrayList<Integer> >();
	// 
	ArrayList<String> tagsList = new ArrayList<String>();
	public String tempTextSnapShot;
	
	
	public AnnotatableDocument getAnnotatableDoc(String fileName)
		{
			Document d = null;
			DocumentBuilder builder = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	
			
			InputStream is = cleanInputStream(fileName);
			try
			{
				builder = dbf.newDocumentBuilder();
				d = builder.parse(is);
			}
			catch(FileNotFoundException fnfe)
			{
				System.out.println("Couldn't find that file in" + System.getProperty("user.dir"));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
			/////// for debugging / testing
			Node c = d.getElementsByTagName("article").item(0);
			tempTextSnapShot = ((Node)c).getTextContent();
			///////
			
		markNestedTags(d);
		getPreDefinedSections(d);
	

		//TODO: change this constructor to take list of sections, also, why "sgml?"
		return new AnnotatableDocument(d,"smgl?");
	}

	//TODO: sustainable conversion
	private InputStream cleanInputStream(String fileName)
	{
		ArrayList<String> fileStuff = null;
		StringBuilder sb = new StringBuilder();
		String type = null;
		
		try {
			fileStuff = (ArrayList<String>)Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Pattern p_link = Pattern.compile("<link[^>]*");
		
		for(String line : fileStuff)
		{
	
			if(line.contains("doctype"))
			{
				type = "sgml";
				line = line.replace("doctype", "DOCTYPE").
						replace("public","PUBLIC").
						replace("[]"," \"dtds/journalpublishing.dtd\"");
			}
			else if(line.contains("DOCTYPE"))
			{
				type = "xml";
				if(!line.contains("dtds/journalpublishing.dtd"))
				{
					line = line.replace("journalpublishing.dtd","dtds/journalpublishing.dtd");
				}
			}
				
			line = p_link.matcher(line).replaceAll(" ");
			line = line.replace("<br>", " ");
			// to help make "text snapshot" more readable/tokenizable
			line = line.replace("<"," <");
			sb.append(line);
			
		}
		
		return new ByteArrayInputStream(sb.toString().getBytes());
		
	}
	
	
	
	
	
	// ~~ Document cleaning, parsing, indexing
	
		
	/**Marks tags indicated in "nestedTags.txt" to not treat them as thier own section*/
	private void markNestedTags(Document d)
	{
		
		// ideally would be able to identify these in a separate parsing task, instead of hardcoding them
		File nestedTagsFile = new File("rules/nestedTags.txt");
		
		////////////////// Save the list of tags in case we want to use them
		try {
			Scanner scan = new Scanner(nestedTagsFile);
			while(scan.hasNextLine())
			{
				tagsList.add(scan.nextLine()); 
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/////////////////////////
		
		// change the "text" of nested nodes to [~~tagname| text |tagname~~]
		for(String tag : tagsList)
		{
			NodeList l = d.getElementsByTagName(tag);
			for(int i = 0; i< l.getLength(); i++)
			{
				Node n = l.item(i);
				StringBuilder sb = new StringBuilder();
				sb.append("[~~");
				sb.append(n.getNodeName());
				sb.append("| ");
				
				sb.append(n.getTextContent());
				
				sb.append(" |");
				sb.append(n.getNodeName());
				sb.append("~~]");
				n.setTextContent(sb.toString());
				
			}
		}
		
	}
	
	
	//TODO: possibly deprecated -- don't need a "text snapshot" if we have an indexed version of the text
	// remove spaces and empty strings from the text
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
	
	@Deprecated
	private void getSections(Node d)
	{
		ArrayList<String> path = new ArrayList<String>();
		path.add(d.getNodeName());

		
		loadup(d, path, "UNASSIGNED");
		sectionNumber = 0;
	}
	@Deprecated
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
								
					
				informalSectionName = getInformalSectionName(tempNode,path,informalSectionName);
					
				loadup(tempNode, path, informalSectionName);
			
				// associate the section number with a SectionContainer with copy of node and the path.
				// deep copied the node to separate the text and attribute data I want from ""s left to remedy double counting entities
				if(!tempNode.getTextContent().equals(""))
				{
					int relevance = getSectionRelevance(path);
					sections.put(sectionNumber, new SectionContainer(
							tempNode.cloneNode(true), path, sectionNumber, relevance));
					
					if(informalSections.isEmpty())
					{
						informalSections.add(informalSectionName);
					}
					if(!informalSections.get(informalSections.size()-1).equals(informalSectionName))
					{
						informalSections.add(informalSectionName);
					}
					
					markInformalSection(informalSectionName);
					sectionNumber++;
				}
				
				// clear out the text at the bottom of the recrusion tree as to not double count as we ascend back up
				tempNode.setTextContent("");
			
				//"pop" off the last layer when you are done with it's children
				if(path.size()>0) path.remove( path.size()-1 );
				
			}	
		}		
	}
	
	
	private void getPreDefinedSections(Node d)
	{
		ArrayList<String> path = new ArrayList<String>();
		
		// pick out the abstract and add it
		NodeList abstracts = ((Document) d).getElementsByTagName("abstract");
		int numberOfAbstracts = abstracts.getLength();
		if(numberOfAbstracts>0)
		{
			numberOfAbstracts--; // to adjust to 0 indexing
			Node abstrackt = abstracts.item(numberOfAbstracts); // appears the last abstract has the most content
			int relevance = getSectionRelevance(path);
			sections.put(sectionNumber, new SectionContainer(
					abstrackt.cloneNode(true), path, sectionNumber, relevance));
			sectionNumber++;
		}
		////////
		
		// pick out "body" and parse it
		Node body = ((Document)d).getElementsByTagName("body").item(0);
		path.add(body.getNodeName());
		loadupPreDefined(body, path);
		
		sectionNumber = 0;
	}
	/**This parses a node with the assumption that all the text content will be in paragraph and title nodes
	 * and that no further granularity (other than "nested sections") is needed
	 */
	 
	private void loadupPreDefined(Node node, ArrayList<String> path)
	{
		NodeList nodeList = node.getChildNodes();
		for (int count = 0; count < nodeList.getLength(); count++) 
		{
			Node tempNode = nodeList.item(count);
			String nodeName = tempNode.getNodeName();

			if(tempNode.getNodeName().equals("p") || tempNode.getNodeName().equals("title"))
			{
				int relevance = getSectionRelevance(path);
				sections.put(sectionNumber, new SectionContainer(
						tempNode.cloneNode(true), path, sectionNumber, relevance));

				sectionNumber++;
				if(path.size()>0) path.remove( path.size()-1 );
				continue;
			}
			
			path.add(tempNode.getNodeName());
			loadupPreDefined(tempNode, path);
			
				
		}		
	}
	
	//TODO: idea undeveloped
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
	
	private void markInformalSection(String informalSectionName)
	{
		if(!informalSectionIndex.containsKey(informalSectionName))
		{
			informalSectionIndex.put(informalSectionName, new ArrayList<Integer>());
		}
		informalSectionIndex.get(informalSectionName).add(sectionNumber);
	}
	

	
	
	
	
	
	///////////// Print utilities
	
	public void printNode(Node node)
	{
		// make sure it's an "element node" as opposed to Type, Notation, Position, etc. .
		if (node.getNodeType() == Node.ELEMENT_NODE) {
	 
			// get node name and value
			System.out.println("\nNode Name =" + node.getNodeName() + " [OPEN]");
			System.out.println("Node Value =" + node.getTextContent());
	 
			if (node.hasAttributes()) {
	 
				// get attributes names and values
				NamedNodeMap nodeMap = node.getAttributes();
	 
				for (int i = 0; i < nodeMap.getLength(); i++) {
	 
					Node nnode = nodeMap.item(i);
					System.out.println("attr name : " + nnode.getNodeName());
					System.out.println("attr value : " + nnode.getNodeValue());
	 
				}
	 
			}
		
	}
}
	
	public void printNodeList(NodeList nodeList) 
	{
		  
	    for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			
			printNode(tempNode);
			if (tempNode.hasChildNodes()) 
			{
					// loop again if has child nodes
					printNodeList(tempNode.getChildNodes());
			}
				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
		}
	    
	    
	}
	

}
	

