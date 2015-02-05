package base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;


public class Visualizer {

	
	public void makeTable(AnnotatableDocument doc, String destinationFileName)
	{
		FileWriter output = null;
			try{ output=new FileWriter(destinationFileName);}
			catch(Exception e){e.printStackTrace();}
			
		String types[] = {"regular","italics"};
		for(String type : types)
		{
			ArrayList<String> sections = new ArrayList<String>();
			HashSet<String> entitiesSet = new HashSet<String>();
			
			Hashtable<Integer, SectionContainer> sectionContainers = doc.getSections();
	 		for(int i = 0; i < sectionContainers.size(); i++)
			{
				SectionContainer sc = sectionContainers.get(i);
				StringBuilder sb = new StringBuilder();
				for( String s : sc.getPath() )
				{
					sb.append(s);
					sb.append("/");
				}
				sections.add( sb.toString() );
			}
	 		
	// 		for( String s : sections )
	// 		{
	// 			System.out.println(s);
	// 		}
			
			ArrayList<Entity> ents = doc.getEntList();
			if(type.equals("italics")) ents = doc.getItalicsList();
			for(Entity e : ents)
			{
				entitiesSet.add(e.getText());
			}
			Object uniqueEntities[] = entitiesSet.toArray();
	//		for(Object o : uniqueEntities)
	//		{
	//			System.out.println(o);
	//		}
			
			
			ArrayList< ArrayList<String> > row = new ArrayList < ArrayList<String> >();
			for(Object ent : uniqueEntities)
			{
				row.add(new ArrayList<String>());
				ArrayList<String> columns = row.get(row.size()-1);
				columns.add( (String)ent);
				for(String section : sections)
				{
					int count = 0;
					for(Entity e : ents)
					{
						//if word matches and section matches, add it up
						if(e.getText().equals(ent)   &&   arrayString(e.getLocation()).equals(section))
						{
							count++;
						}
					}
					columns.add( String.valueOf(count) );
				}
			}
			
			
			
			// Write the html table
			
			try {
	
				output.write("<TABLE BORDER CELLPADDING=3");
				output.write("<TR> <TH> Entity</TH>");
				
				for(String section : sections)
				{
					output.write("<TH>"+section+"</TH> ");
				}
				output.write("</TR>");
				
				for(ArrayList<String> columns : row)
				{
					output.write("<TR>");
					for(String i : columns)
					{
						output.write("<TD>"+i+"</TD>");
					}
					output.write("</TR>");
				}
				output.write("</TABLE>");
				output.write("<BR><BR><BR>");
				
				if(type.equals("italics")) output.close();
				
				
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		

		
	}
	
	private String arrayString(ArrayList<String> array)
	{
		StringBuilder sb = new StringBuilder();
		for(String s : array )
		{
			sb.append(s);
			sb.append("/");
		}
		return sb.toString();
	}
}
