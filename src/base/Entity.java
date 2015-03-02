package base;
import java.util.ArrayList;

import l.EntAnnotation;


public class Entity {
	private String text;
	private SectionContainer location;
	private int startPosition;
	private int endPosition;
	private double probability;
	private int id; //not sure if this is necessary. EntList will associate an id
	
	private ArrayList<String> foundByList;
	
	private EntAnnotation eAnnotation;
	
//	Entity()
//	{
//		text = null;
//		location = null;
//		position = -1;
//	}
	
	Entity(String _text, SectionContainer _location, int start, int end)
	{
		text = _text;
		location = _location;
		startPosition = start;
		endPosition = end;
		
		foundByList = new ArrayList<String>();
		
		eAnnotation = new EntAnnotation();
	}
	public void addFoundBy(String ruleName)
	{
		foundByList.add(ruleName);
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<String> getLocation() {
		return location.getPath();
	}

	public void setLocation(ArrayList<String> location) {
		this.location.setPath(location);
	}

	public double getPosition() 
	{
		return startPosition;
	}

	public void setPosition(int position) 
	{
		this.startPosition = position;
	}
	
	public String toString()
	{
		return text + "\n" + location.toString() + " @" + startPosition;
	}

	public int getSectionNumber()
	{
		return location.getSectionNumber();
	}
	
	public ArrayList<String> getFoundByList()
	{
		return foundByList;
	}
	
	public void setStatus(EntAnnotation.Status s)
	{
		eAnnotation.mark(s);
	}
	
	public void setId(int _id)
	{
		id=_id;
	}
	public int getId()
	{
		return id;
	}
}
