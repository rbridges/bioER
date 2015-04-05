package base;
import java.util.ArrayList;

import org.w3c.dom.Node;


public class SectionContainer {
	private Node n;
	private ArrayList<NodeBundle> path;
	private String text;
	private int sectionNumber;
	String informalSectionName;
	
	private int relevance;
//	
//	public Node getN() {
//		return n;
//	}

	public ArrayList<NodeBundle> getPath() {
		return path;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public int getSectionNumber()
	{
		return sectionNumber;
	}

	class stats
	{
		//some filter for displaying which entities appear here and how many, etc
	}
	


	
	public SectionContainer(Node _n, ArrayList<NodeBundle> _path, String _informalSectionName,
			int _sectionNumber, int _relevance)
	{
		n = _n;
		sectionNumber = _sectionNumber;
		path = new ArrayList<NodeBundle> (_path);
		informalSectionName = _informalSectionName;
		text = new String(n.getTextContent());
		relevance = _relevance;
	//	System.out.println(n.getNodeName() + " : " + text + " @ " + path);
		 
	}
	
	public int getRelevance()
	{
		return relevance;
	}
	public String getInformalSectionName()
	{
		return informalSectionName;
	}
	
	public String toString()
	{
		if(path.size() == 0)
		{
			return "empty";
		}
		
		StringBuilder sb = new StringBuilder();
		for(NodeBundle nb : path)
		{
			String s = nb.toString();
			sb.append(s);
			sb.append("/");
		}
		return sb.toString();
	}
}
