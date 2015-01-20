import java.util.ArrayList;

import org.w3c.dom.Node;


public class SectionContainer {
	private Node n;
	private ArrayList<String> path;
	private String text;
	
	public Node getN() {
		return n;
	}

	public void setN(Node n) {
		this.n = n;
	}

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

	class stats
	{
		//some filter for displaying which entities appear here and how many, etc
	}
	



	// TODO: get rid of this
	class Section {
		
		ArrayList<String> location;
		
		Section(){} //TODO: this enables null location. Why do I need it?
		Section(ArrayList<String> _location)
		{
			location = _location;
		}
		
		public ArrayList<String> getSection()
		{
			return location;
		}
		

		
	}

	
	SectionContainer(Node _n, ArrayList<String> _path)
	{
		n = _n;
		path = new ArrayList<String> (_path);
		text = new String(n.getTextContent());
	//	System.out.println(n.getNodeName() + " : " + text + " @ " + path);
		 
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
