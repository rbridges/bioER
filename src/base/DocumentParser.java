package base;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
	
	// to cache already seen rules. A map between a filename and a List of Rules
		
	

	// derived from example at http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	public AnnotatableDocument getAnnotatableDoc(String fileName)
		{
		
		//InputStream is = new ByteArrayInputStream(str.getBytes());
			Document d = null;
			DocumentBuilder builder = null;
			InputStream is = cleanInputStream(fileName);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	
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
		System.out.println(fileStuff.size());
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
			sb.append(line);
			
		}
		
		return new ByteArrayInputStream(sb.toString().getBytes());
		
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
	

