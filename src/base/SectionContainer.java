package base;
import java.util.ArrayList;

import org.w3c.dom.Node;


public class SectionContainer {
	private Node n;
	private ArrayList<String> path;
	private String text;
	private int sectionNumber;
	
	private int relevance;
//	
//	public Node getN() {
//		return n;
//	}

	public ArrayList<String> getPath() {
		return path;
	}

	public void setPath(ArrayList<String> path) {
		this.path = path;
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
	


	
	public SectionContainer(Node _n, ArrayList<String> _path, int _sectionNumber, int _relevance)
	{
		n = _n;
		sectionNumber = _sectionNumber;
		path = new ArrayList<String> (_path);
		text = new String(n.getTextContent());
		relevance = _relevance;
	//	System.out.println(n.getNodeName() + " : " + text + " @ " + path);
		 
	}
	
	public int getRelevance()
	{
		return relevance;
	}
	
	@Override
	public String toString()
	{
		if(path.size() == 0)
		{
			return "empty";
		}
		
		StringBuilder sb = new StringBuilder();
		for(String s : path)
		{
			sb.append(s);
			sb.append("/");
		}
		return sb.toString();
	}
}
