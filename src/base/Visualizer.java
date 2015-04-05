package base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import utils.NameSet;


public class Visualizer {

	
	public void makeTable_Old(AnnotatableDocument doc, String destinationFileName)
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
				for( NodeBundle nb : sc.getPath() )
				{
					String s = nb.getName();
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
	
	public void makeTable(AnnotatableDocument doc, String destinationFileName)
	{
		FileWriter output = null;
			try{ output=new FileWriter(destinationFileName);}
			catch(Exception e){e.printStackTrace();}
			
		String types[] = {"regular","italics"};
		for(String type : types)
		{
			
			HashSet<String> entitiesSet = new HashSet<String>();
			
			//// getting the integers that identify 
			Hashtable<Integer, SectionContainer> sectionContainers = doc.getSections();
			Enumeration<Integer> k = sectionContainers.keys();
			ArrayList<Integer> sectionNumbers = new ArrayList<Integer>();
			while(k.hasMoreElements())
			{
				sectionNumbers.add(k.nextElement());
			}
			
			ArrayList<Entity> ents = doc.getEnts("regular");
			if(type.equals("italics")) ents = doc.getEnts("italics");
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
				for(int section=0; section<sectionContainers.size(); section++)
				{
					int count = 0;
					for(Entity e : ents)
					{
						//if word matches and section matches, add it up
						if(e.getText().equals(ent)   &&   e.getSectionNumber()==section )
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
				
				for(int sectionNum=0; sectionNum<sectionContainers.size(); sectionNum++)
				{
					output.write("<TH>"+sectionContainers.get(sectionNum).toString()+"</TH> ");
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
	
	public void makeCompactTable(AnnotatableDocument d, String destinationFile) throws IOException
	{

		FileWriter fw = null;
		try{ fw=new FileWriter(destinationFile);}
		catch(Exception e){e.printStackTrace();}
		
		
		ArrayList<String> informalSections = d.getInformalSectionLookup();
		EntManager manager = d.getEntManager();
		
		Hashtable<String,ArrayList<Integer> > rows = new Hashtable<String,ArrayList<Integer> > (); 
		// for all names of entities
		for(String entName : manager.getUniques())
		{
			rows.put(entName, new ArrayList<Integer>() );
			int total = 0;
			//for all informal section names
			for(String section : informalSections)
			{
				int count = 0;
				// for each actual section number in that informal name
				for(int sectionNumber : d.getSectionsByInformalName(section))
				{
					EntList eList = manager.getEntList();
					ArrayList<Entity> entsInSection= eList.getBySection(sectionNumber);
					if(entsInSection==null) continue;
					
					//for each entity in a specific section
					for(Entity e : entsInSection )
					{
						if( manager.getMainName( e.getText() ).equals(entName) )
						{
							count++;
						}
					}
				}
				//add the count to the vector associated with the entName
				rows.get(entName).add(count);
				total += count;
			}
			//put the total count of all of the instances of that entity in the last entry of the vector
			rows.get(entName).add(total);
		}
		
		fw.write("<TABLE BORDER CELLPADDING=3");
		fw.write("<TR> <TH> Entity</TH> <TH> Total </TH>");
		int a = 1;
		for(String section : informalSections)
		{
			String section2 = "";
			if(!section.equals("UNASSIGNED")) 
			{
				section2 = " ("+section+")";
			}
			
			fw.write("<TH> Section " + a++ + section2+ "</TH>");
		}
		fw.write("</TR>");
		for(Entry<String, ArrayList<Integer>> s : rows.entrySet())
		{
			System.out.println(s);
		}
		
		int maxNum=0;
		String maxKey="";
		while(!rows.isEmpty())
		{
			maxNum=0;
			maxKey="";
			for(String entName : rows.keySet())
			{
				ArrayList<Integer> entOccurances = rows.get(entName);
				if( entOccurances.get( entOccurances.size()-1 ) >= maxNum)
				{
					maxKey = entName;
					maxNum = entOccurances.get(entOccurances.size()-1);
				}	
			}
			fw.write("<TR><TD><FONT COLOR=\"BLUE\">"+maxKey+"</FONT></TD><TD><FONT COLOR=\"BLUE\">"+maxNum);
			ArrayList<Integer> occurances = rows.get(maxKey);
			occurances.remove( occurances.size()-1 );
			for(int occurance : occurances)
			{
				fw.write("</FONT><TD>"+occurance+"</TD>");
			}
			fw.write("</TR>");
			rows.remove(maxKey);
		}

		
		fw.write("</TABLE>");
		fw.close();
//		
//		for(String section : informalSections)
//		{
//			NameSet<Entity> nameSet = new NameSet<Entity>();
//			//System.out.println("In " + section + ": \n");
//			fw.write("In " + section + ": \n");
//				
//			for(int sectionNumber : d.getSectionsByInformalName(section))
//			{
//				EntList eList = manager.getEntList();
//				ArrayList<Entity> entsInSection= eList.getBySection(sectionNumber);
//				if(entsInSection==null) continue;
//				
//				for(Entity e : entsInSection )
//				{
//					nameSet.insert(manager.getMainName(e.getText()), e);
//				}
//			}
//			for(String name : nameSet.getItems())
//			{
//				//System.out.println("\t"+name+"("+nameSet.getFrequency(name)+
//						//") w/ aliases " + manager.getAliases(name));
//				fw.write("\t"+name+"("+nameSet.getFrequency(name)+
//						") w/ aliases " + manager.getAliases(name)+"\n");
//				
//			}
//			//System.out.println("\n\n");
//			fw.write("\n\n");
//		}
//		
		
	}
	
	private String arrayString(ArrayList<NodeBundle> array)
	{
		StringBuilder sb = new StringBuilder();
		for(NodeBundle nb : array )
		{
			String s = nb.getName();
			sb.append(s);
			sb.append("/");
		}
		return sb.toString();
	}
}
