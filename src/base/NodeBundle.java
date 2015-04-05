package base;

import java.util.Hashtable;

import org.w3c.dom.Node;

/** Used for easily grouping the names and attributes of nodes in document*/
public class NodeBundle 
{
	String name;
	Hashtable<String,String> attributes = new Hashtable<String,String>();
	
	public NodeBundle(String _name)
	{
		name = _name;
	}
	
	public NodeBundle(Node body) 
	{
		name = body.getNodeName();
		if( body.hasAttributes() )
		{
			String attPair = body.getAttributes().item(0).toString();
			String pair[] = attPair.split("=");
			attributes.put(pair[0], pair[1].replaceAll("[\"|\"]", "") ); //remove quotes
		}
		
	}

	public void addAttribute(String key, String value)
	{
		value = value.replaceAll("[\"|\"]", ""); //remove quotes
		attributes.put(key,value);
	}
	
	public String getName()
	{
		return name;
	}
	public boolean hasAttribute()
	{
		if(attributes.isEmpty()) return false;
		return true;
	}
	public String getAttribute(String att)
	{
		if(attributes.containsKey(att) ) return attributes.get(att);
		return null;
	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		if( hasAttribute() )
		{
			// only keep track of one attribute for extra differentiation
			String anAttribute = attributes.keys().nextElement();
			sb.append(" ");
			sb.append(anAttribute);
			sb.append("=\"");
			sb.append(attributes.get(anAttribute));
			sb.append("\"");
		}
		
		return sb.toString();
	}
	
}
