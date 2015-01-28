import inclusionRules.CategoryKeywordMatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import org.w3c.dom.Document;

import base.AnnotatableDocument;
import base.EntList;
import base.EntManager;
import base.Entity;
import base.DocumentParser;


public class bioER {
	public static void main(String argv[])
	{
		Scanner scan = new Scanner(System.in);
		DocumentParser p = new DocumentParser();
		AnnotatableDocument d = p.getAnnotatableDoc(scan.next());
		//d = p.copy(d);
		//p.printNode(d);
		
		
		for (int i = 0; i<d.getSections().size();i++)
		{
			System.out.println( d.getSections().get(i).getN().getTextContent() );
		}
		//demo(p);
	}
	
	
//	public static void demo(DocumentParser p)
//	{
//		Scanner scan = null;
//		EntManager eManager = new EntManager();
//		try 
//		{
//			scan = new Scanner(new File ("regexPatterns.txt"));
//		} catch (FileNotFoundException e) 
//		{
//			e.printStackTrace();
//			System.exit(1);
//		}
//		// give the Entity Manager regex based rules to discover Entity candidates
//		while(scan.hasNextLine())
//		{
//			eManager.addRule( scan.nextLine() );
//		}
//		eManager.addRule(new CategoryKeywordMatcher("keywords.txt"));
//		
//		//go through sections and pick out tokens that match the rules added above
//		eManager.loadup(d.getSections());
//		
//		EntList entities = eManager.getEntList();
//		for( Entity e : entities.getEntList() )
//		{
//			System.out.println(e);
//		}
//		}
}
