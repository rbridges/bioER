import java.util.ArrayList;

import org.w3c.dom.Node;


public class SectionContainer {
	Node n;
	ArrayList<String> path;
	String text;
	
	class stats
	{
		//some filter for displaying which entities appear here and how many, etc
	}
	
	SectionContainer(Node _n, ArrayList<String> _path)
	{
		n = _n;
		path = new ArrayList<String> (_path);
		text = new String(n.getTextContent());
		System.out.println(n.getNodeName() + " : " + text + " @ " + path);
		 
	}
}
