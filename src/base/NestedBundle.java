package base;

public class NestedBundle 
{
	private int start;
	private int end;
	private String name;
	private int id;
	
	public NestedBundle(int _start, String _name, int _id)
	{
		start = _start;
		name = _name;
		id = _id;
	}
	public void setEnd(int _end)
	{
		end = _end;
	}
	
	
	
	
	public int getStart()
	{
		return start;
	}
	public int getEnd()
	{
		return end;
	}
	
	public String toString()
	{
		return name;
	}
}
