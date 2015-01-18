import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


public class Parser {
	Hashtable<Integer, SectionContainer> sections = new Hashtable<Integer, SectionContainer>();
	int number;
	
	Parser()
	{
		number = 0;
	}
	// derived from example at http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	public Document domifyDocument()
		{
			Document d = null;
		
		try
		{
			Scanner scan = new Scanner(System.in);
			System.out.println("Gimmie a filename");
			File f = new File(scan.next());
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();

			d = builder.parse(f);
		
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Didn't find that file in this directory. Try another?\n\n");
			domifyDocument();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return d;
	}
	
	
	
	
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
	
	public void printNodeList(NodeList nodeList) {
		  
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
	
	public Document copy(Document d)
	{
		d.compareDocumentPosition(d);
		d.getParentNode();
		//d.setUserData(key, data, handler); 
		//d.getUserData(key);
		d.setTextContent("");
		return d;
	}
	
	public void loadup(NodeList nodeList)
	{
		ArrayList<String> path = new ArrayList<String>();
		path.add(nodeList.item(0).getNodeName());
		loadup(nodeList, path);
		number = 0;
	}
	private void loadup(NodeList nodeList, ArrayList<String> path)
	{
		for (int count = 0; count < nodeList.getLength(); count++) {
			
			Node tempNode = nodeList.item(count);
			// make sure it's an "element node" as opposed to Type, Notation, Position, etc. .
			//if (!(tempNode.getNodeType() == Node.ELEMENT_NODE) ) continue;
			
			if (tempNode.hasChildNodes())
			{
				path.add(tempNode.getNodeName());
				loadup(tempNode.getChildNodes(), path );
			
				// associate the section number with a SectionContainer with copy of node and the path.
				// deep copied the node to separate the text and attribute data I want from ""s left to remedy double counting entities
				if(!tempNode.getTextContent().equals(""))
				{
					sections.put(number, new SectionContainer(tempNode.cloneNode(true), path));
					number++;
				}
				// clear out the text at the bottom of the recrusion tree as to not double count as we ascend back up
				tempNode.setTextContent("");
			
			
//				if(path.size() == 0){
//					for(int j = 0; j < sections.size(); j++)
//					{
//						SectionContainer sc = sections.get(j);
//						System.out.println(sc.text);
//						for(String s : sc.path)
//						{
//							System.out.print(s+"/");
//						}
//						System.out.println();
//						
//					}
//					sections.getClass();	
//				}
				//"pop" off the last layer when you are done with it's children
				path.remove( path.size()-1 );
		}
			
		}
			
	}

}
	

