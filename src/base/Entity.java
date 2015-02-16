package base;
import java.util.ArrayList;

//TODO: how to deal with alias network? Probably don't
// want it inside Entity class, but want it accessible from
public class Entity {
	private String text;
	private SectionContainer location;
	private int startPosition;
	private int endPosition;
	private double probability;
	private Long id; //not sure if this is necessary. EntList will associate an id
	
	private ArrayList<String> foundByList;
	
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
	}
	public void addFoundBy(String ruleName)
	{
		foundByList.add(ruleName);
	}

	public String getText() {
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

	public double getPosition() {
		return startPosition;
	}

	public void setPosition(int position) {
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
}
