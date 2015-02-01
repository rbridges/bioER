import inclusionRules.CategoryKeywordMatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import org.w3c.dom.Document;

import base.AnnotatableDocument;
import base.Annotator;
import base.EntList;
//import base.EntManager;
import base.Entity;
import base.DocumentParser;


public class bioER {
	public static void main(String argv[])
	{
		demo2();
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
	
	public static void demo2()
	{
		Scanner scan = new Scanner(System.in);
		DocumentParser p = new DocumentParser();
		Annotator annotator = new Annotator();
		System.out.println("Give an xml or gml filename: ");
		AnnotatableDocument d = p.getAnnotatableDoc(scan.next());
		
		annotator.annotate(d, "rules/regexPatterns.txt" );
		annotator.remove(d, "rules/killList.txt");
		annotator.metaData(d, "rules/keywords.txt");
		
		for(Entity e : d.getEntList() )
		{
			System.out.println(e+"\n");
		}
		
		Hashtable<String, Integer> metadata = d.getMetaData();
		for(String s : metadata.keySet() )
		{
			System.out.println(s+" has mentions: "+metadata.get(s));
		}
		
		
		
	}
}
