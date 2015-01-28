package base;
import inclusionRules.CategoryKeywordMatcher;
import inclusionRules.InclusionRule;
import inclusionRules.RegexRule;

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



public class DocumentParser {
	
	// to cache already seen rules. A map between a filename and a List of Rules
	Hashtable< String,ArrayList<Rule> > rules; 	
	
	public DocumentParser()
	{
		rules = new Hashtable< String,ArrayList<Rule> >();
	}

	// derived from example at http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	public AnnotatableDocument getAnnotatableDoc(String fileName)
		{
			Document d = null;
			File file = new File(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try
			{
				DocumentBuilder builder = dbf.newDocumentBuilder();
				d = builder.parse(file);
			}
			catch(FileNotFoundException fnfe)
			{
				System.out.println("Couldn't find that file in this directory. Enter another.");
				Scanner scan = new Scanner(System.in);
				getAnnotatableDoc(scan.next());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		return new AnnotatableDocument(d);
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
	
	public void annotate(AnnotatableDocument aDoc, String fileName)
	{
		if(!rules.containsKey(fileName) )
		{
			addRules(fileName);
		}
		
	}
	
	// probably depricate this in favor of the file method, but good for intermediate use
	public void annotate(AnnotatableDocument aDoc, Rule rule, String token)
	{
		if(rule instanceof CategoryKeywordMatcher)
		{
			CategoryKeywordMatcher ckm = (CategoryKeywordMatcher) rule;
			String category = ckm.getKeywordMap().get(token);
			if(category == null)
			{
				return;
			}
			aDoc.addMetaData(category);
		}
	}

	// currently only adds regex rules
	public void addRules(String fileName) 
	{
		File f = new File(fileName);
		Scanner scan = null;
		
		//////////
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//////////
		
		ArrayList<Rule> fileRules = new ArrayList<Rule>();
		
		// could add a markup in the file to get a different add methodology
		while(scan.hasNext())
		{
			String regex = scan.next();
			fileRules.add( new RegexRule(regex) );
		}
		
		rules.put(fileName, fileRules);
		
	}
	
	private void rollThrough_booleanRule(AnnotatableDocument ad, String ruleKey)
	{
		Hashtable<Integer, SectionContainer> sections = ad.getSections();
		
		ArrayList<Rule> fetchedRules = rules.get(ruleKey);
		for(Rule r : fetchedRules)
		{
			for(SectionContainer sc : sections.values())
			{
				String t[] = sc.getText().split(" ");
				for(int i = 0; i < t.length; i++ )
				{
					if( ((InclusionRule) r).isEnt(t[i]))
					{
						ad.getEntList().add( new Entity(t[i], sc, i) );
					}
				}	
			}
		}
		
	}
	

}
	

