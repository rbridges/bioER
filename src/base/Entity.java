package base;
import java.util.ArrayList;

//TODO: how to deal with alias network? Probably don't
// want it inside Entity class, but want it accessible from
public class Entity {
	String text;
	SectionContainer location;
	int position;
	double probability;
	Long id; //not sure if this is necessary. EntList will associate an id
	
	Entity()
	{
		text = null;
		location = null;
		position = -1;
	}
	
	Entity(String _text, SectionContainer _location, int _position)
	{
		text = _text;
		location = _location;
		position = _position;
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
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public String toString()
	{
		return text + "\n" + location.toString() + " @" + position;
	}

	
	
}
