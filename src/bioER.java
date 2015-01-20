import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import org.w3c.dom.Document;


public class bioER {
	public static void main(String argv[])
	{
		Parser p = new Parser();
		Document d = p.domifyDocument();
		//d = p.copy(d);
		//p.printNode(d);
		
		p.loadup(d.getChildNodes());
//		for (int i = 0; i<p.sections.size();i++)
//		{
//			System.out.println( p.sections.get(i).getN().getTextContent() );
//		}
		demo(p);
		
	}
	
	
	public static void demo(Parser p)
	{
		Scanner scan = null;
		EntManager eManager = new EntManager();
		try 
		{
			scan = new Scanner(new File ("regexPatterns.txt"));
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		// give the Entity Manager regex based rules to discover Entity candidates
		while(scan.hasNextLine())
		{
			eManager.addRule( scan.nextLine() );
		}
		
		//go through sections and pick out tokens that match the rules added above
		eManager.loadup(p.sections);
		
		EntList entities = eManager.entList;
		for( Entity e : entities.getEntList() )
		{
			System.out.println(e);
		}
		
		
		
		
		
		
	}
}
