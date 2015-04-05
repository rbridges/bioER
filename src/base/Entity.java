package base;
import java.util.ArrayList;

import annotation.EntAnnotation;


public class Entity {
	private String text;
	private String type;
	private int sectionNumber;
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
	Entity(String _text, int _sectionNumber, int start, int end)
	{
		text = _text;
		sectionNumber = _sectionNumber;
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

	public ArrayList<NodeBundle> getLocation() {
		return location.getPath();
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
		return sectionNumber;
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
	public void setIsA(String _type) {
		type = _type;
	}
	
	public String getType()
	{
		return type;
	}
}
