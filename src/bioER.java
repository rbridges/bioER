import org.w3c.dom.Document;


public class bioER {
	public static void main(String argv[])
	{
		Parser p = new Parser();
		Document d = p.domifyDocument();
		//d = p.copy(d);
		p.printNode(d);
		
		if(d.hasChildNodes())
		{
			//p.printNodeList(d.getChildNodes());
		}
		
		p.loadup(d.getChildNodes());
		for (int i = 0; i<p.sections.size();i++)
		{
			System.out.println( p.sections.get(i).n.getTextContent() );
		}
		
		

		
	}
}
