import inclusionRules.CategoryKeywordMatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;

import org.w3c.dom.Document;

import utils.NameSet;
import utils.NumberedSet;
import base.AnnotatableDocument;
import base.Annotator;
import base.EntList;
import base.EntManager;
//import base.EntManager;
import base.Entity;
import base.DocumentParser;
import base.Visualizer;


public class bioER {
	public static void main(String argv[])
	{
		// inputFiles/xml/PC_108688_fin.xml
		
		try {
			demo5();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
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
	
	public static void demo3()
	{
		Scanner scan = new Scanner(System.in);
		DocumentParser p = new DocumentParser();
		Annotator annotator = new Annotator();
		
		Visualizer v = new Visualizer();
		
		System.out.println("Give an xml or gml filename: ");
		AnnotatableDocument d = p.getAnnotatableDoc(scan.next());
		
		annotator.annotate(d, "rules/regexPatterns.txt" );
		annotator.remove(d, "rules/killList.txt");
		annotator.metaData(d, "rules/keywords.txt");
		
		v.makeTable(d, "tableOutput.html");
		
		
	}
	
	public static void demo4()
	{
		Scanner scan = new Scanner(System.in);
		DocumentParser p = new DocumentParser();
		Annotator annotator = new Annotator();
		
		Visualizer v = new Visualizer();
		
		System.out.println("Give an xml or gml filename: ");
		AnnotatableDocument d = p.getAnnotatableDoc(scan.next());
		
		annotator.annotate(d, "rules/regexPatterns.txt" );
		annotator.remove(d, "rules/killList.txt");
		annotator.metaData(d, "rules/keywords.txt");
		
//		for(Entity e : d.getEntList())
//		{
//			System.out.println(d.eList.getByName(e.getText()) );
//		}
		
		d.consolodateAdjacents();
	}
	
	public static long demo5() throws IOException
	{// inputFiles/xml/PC_108688_fin.xml
		
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Give an xml or gml filename: ");
		String fileName = scan.next();
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName+"_OUT");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		long startTime = System.currentTimeMillis();
		DocumentParser p = new DocumentParser();
		Annotator annotator = new Annotator();

		AnnotatableDocument d = p.getAnnotatableDoc(fileName);
	
		annotator.annotate(d);
	
	
//		for(Entity e : em.getEntList().getEntList())
//		{
//			if(e.getFoundByList().contains("bold"))
//			{
//				System.out.println(e.getText());
//			}
//		}
		ArrayList<String> informalSections = d.getInformalSectionLookup();
		EntManager manager = d.getEntManager();
		for(String section : informalSections)
		{
			NameSet<Entity> nameSet = new NameSet<Entity>();
			//System.out.println("In " + section + ": \n");
			fw.write("In " + section + ": \n");
			
			for(int sectionNumber : d.getSectionsByInformalName(section))
			{
				EntList eList = manager.getEntList();
				ArrayList<Entity> entsInSection= eList.getBySection(sectionNumber);
				if(entsInSection==null) continue;
				
				for(Entity e : entsInSection )
				{
					nameSet.insert(manager.getMainName(e.getText()), e);
				}
			}
			for(String name : nameSet.getItems())
			{
				//System.out.println("\t"+name+"("+nameSet.getFrequency(name)+
						//") w/ aliases " + manager.getAliases(name));
				fw.write("\t"+name+"("+nameSet.getFrequency(name)+
						") w/ aliases " + manager.getAliases(name)+"\n");
				
			}
			//System.out.println("\n\n");
			fw.write("\n\n");
		}
		
		fw.close();
		return startTime;
	}

}
