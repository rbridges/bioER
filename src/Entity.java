import java.util.ArrayList;


public class Entity {
	String text;
	ArrayList<String> location;
	double position;
	double probability;
	
	Entity()
	{
		text = "";
		location = new ArrayList();
		position = -1;
	}
	
	Entity(String _text, ArrayList _location, int _position)
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

	public ArrayList getLocation() {
		return location;
	}

	public void setLocation(ArrayList location) {
		this.location = location;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}
	
	public String toString()
	{
		String loc = "";
		for(String l : location)
		{
			loc.concat(l+"/");
		}
		
		return text + "\n" + loc + " @" + position;
	}

	
	
}
